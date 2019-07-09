"""
Copyright 2015 Google, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


Implements an RST documentation emitter for YANG modules

"""
import os

from jinja2 import Environment, FileSystemLoader
from rstcloth.rstcloth import RstCloth

import html_helper
import yangpath
from doc_emitter import DocEmitter
from yangdoc_defs import YangDocDefs


class RSTEmitter(DocEmitter):

    def genModuleDoc(self, mod, ctx):
        # d.title('Example Use')
        # d.newline()
        # d.h2('Contents')
        # d.directive(name="contents", fields=[('local', ''), ('backlinks', 'None')])
        # d.newline()
        # d.h2('Code -- shebang')
        # d.codeblock('#!/usr/bin/env')
        #
        # d.print_content()

        mod_r = RstCloth()
        mod_r.newline()
        mod_r.h1(mod.module_name)
        mod_r.newline()

        if mod.module.attrs.has_key('version'):
            mod_r.h4("openconfig-version: " + mod.module.attrs['version'])

        # module description header
        mod_r.h4("Description")
        mod_r.newline()

        # module description text
        paragraphs = text_to_paragraphs(mod.module.attrs['desc'])
        for para in paragraphs:
            mod_r.content(para)
            mod_r.newline()

        mod_r.h4("Imports")
        mod_r.newline()
        for i in mod.module.attrs['imports']:
            mod_r.content(i)
            mod_r.newline()

        # initialize and store in the module docs
        print('\n'.join(mod_r._data).encode('utf-8'))
        # gen_nav_tree(self, mod, 0)

    def create_links(self, use_cases):
        ht = html_helper.HTMLHelper()
        output = ""
        for use_case in use_cases:
            url = self.moduledocs[u'frinx-openconfig-uc-' + use_case]['reference']
            new_link = ht.add_tag("a", use_case, {"href": url})
            output = output + " | " + new_link
        return output

    def genStatementDoc(self, statement, ctx, level=1):
        """HTML emitter for module data node given a StatementDoc
        object"""

        if ctx.opts.no_structure and statement.keyword in ctx.skip_keywords:
            return
        s_r = RstCloth()

        ht = html_helper.HTMLHelper()

        s_div = ht.open_tag("div", {"class": "statement-section"}, newline=True)

        if ctx.opts.strip_namespace:
            pathstr = yangpath.strip_namespace(statement.attrs['path'])
        else:
            pathstr = statement.attrs['path']

        # for 'skipped' nodes, just print the path
        if statement.keyword in self.path_only:
            s_div += ht.h4(pathstr, None, level, True)
            s_r.newline()
            s_r.content(pathstr)
            s_div += ht.close_tag(newline=True)
            return s_div

        # statement path and name
        (prefix, last) = yangpath.remove_last(pathstr)
        prefix_name = ht.add_tag("span", prefix + "/", {"class": "statement-path"})
        statement_name = prefix_name + ht.br(level, True) + statement.name
        if statement.attrs.has_key('frinx-documentation') or statement.attrs.has_key('frinx-usecase'):
            s_div += ht.h4(statement_name, {"class": "frinx-text-color ", "id": statement.attrs['id']}, level, True)
        else:
            s_div += ht.h4(statement_name, {"class": "statement-name", "id": statement.attrs['id']}, level, True)
        s_r.newline()
        s_r.content(prefix)
        s_r.newline()
        s_r.r_heading_level = r_heading_level
        s_r.r_heading_level(s_r,last,level+6)
        s_r.newline()

        # node description
        if statement.attrs.has_key('desc'):
            s_div += ht.para(
                ht.add_tag("span", "description", {"class": "statement-info-label"}) + ":<br />" + statement.attrs[
                    'desc'], {"class": "statement-info-text"}, level, True)
        s_div += ht.close_tag(newline=True)

        # all FRINX prefixes from units
        prefixes = ["frinx-oc-ios-docs", "frinx-oc-ios-xr-docs", "frinx-oc-ironware-docs", "frinx-oc-nos-docs",
                    "frinx-oc-vrp-docs", "frinx-oc-nexus-docs", "frinx-oc-junos-docs", "frinx-oc-xr-docs"]

        # frinxdoc (added by ab@frinx)
        if statement.attrs.has_key('frinx-documentation'):
            for prefix in prefixes:

                if statement.attrs['frinx-documentation'].has_key(prefix):
                    if prefix == "frinx-oc-junos-docs" or prefix == "frinx-oc-xr-docs":
                        protocol = 'netconf'
                    else:
                        protocol = 'cli'
                    s_div += ht.h4( protocol + " device " + statement.attrs['frinx-documentation'][prefix]['frinx-docs-type'] + ":",
                                    {"class": "frinx-text-color thick frinx-margin-left-medium",
                                     "id": "ident-" + ht.gen_html_id(prefix)}, 2, True)
                    s_div += ht.para(
                        ht.add_tag("span", "frinx-device-type", {"class": "statement-info-label"}) + ":<br />" +
                        statement.attrs['frinx-documentation'][prefix]['frinx-docs-type'],
                        {"class": "statement-info-text frinx-margin-left-medium"}, level, True)
                    s_div += ht.para(
                        ht.add_tag("span", "frinx-supported-versions", {"class": "statement-info-label"}) + ":<br />" +
                        statement.attrs['frinx-documentation'][prefix]['frinx-docs-version'],
                        {"class": "statement-info-text frinx-margin-left-medium"}, level, True)

                    if statement.attrs['frinx-documentation'][prefix].has_key('frinx-docs-reader'):
                        s_div += ht.para(ht.add_tag("span", "frinx-implemented-reader",
                                                    {"class": "statement-info-label"}) + ":<br />" +
                                         statement.attrs['frinx-documentation'][prefix]['frinx-docs-reader'],
                                         {"class": "statement-info-text frinx-margin-left-medium"}, level, True)
                        if statement.attrs['frinx-documentation'][prefix].has_key('frinx-docs-reader-detail'):
                            s_div += ht.para(ht.add_tag("span", "frinx-implemented-reader-details",
                                                        {"class": "statement-info-label"}) + ":<br />" +
                                             statement.attrs['frinx-documentation'][prefix]['frinx-docs-reader-detail'],
                                             {"class": "statement-info-text frinx-preserve-text frinx-margin-left-big"},
                                             level, True)
                    else:
                        if statement.attrs['frinx-documentation'][prefix].has_key('frinx-docs-writer'):
                            if not (statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer'] == "io.frinx.cli.unit.utils.NoopCliWriter" or statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer'] == "io.frinx.unitopo.unit.utils.NoopWriter" or statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer'] == "io.frinx.cli.unit.utils.NoopCliListWriter" or statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer'] == "io.frinx.unitopo.unit.utils.NoopListWriter") :
                                s_div += ht.para(ht.add_tag("span", "frinx-implemented-reader",
                                                            {"class": "statement-info-label"}) + ":<br />" +
                                                 "MISSING READER",
                                                 {"class": "statement-info-text frinx-margin-left-medium"}, level, True)

                    if statement.attrs['frinx-documentation'][prefix].has_key('frinx-docs-writer'):
                        if statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer'] != "io.frinx.cli.unit.utils.NoopCliWriter" and statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer'] != "io.frinx.unitopo.unit.utils.NoopWriter" and statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer'] != "io.frinx.cli.unit.utils.NoopCliListWriter" and statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer'] != "io.frinx.unitopo.unit.utils.NoopListWriter"  :
                            s_div += ht.para(ht.add_tag("span", "frinx-implemented-writer",
                                                        {"class": "statement-info-label"}) + ":<br />" +
                                             statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer'],
                                             {"class": "statement-info-text frinx-margin-left-medium"}, level, True)
                            if statement.attrs['frinx-documentation'][prefix].has_key('frinx-docs-writer-detail'):
                                s_div += ht.para(ht.add_tag("span", "frinx-implemented-writer-details",
                                                            {"class": "statement-info-label"}) + ":<br />" +
                                                 statement.attrs['frinx-documentation'][prefix]['frinx-docs-writer-detail'],
                                                 {"class": "statement-info-text frinx-preserve-text frinx-margin-left-big"},
                                                 level, True)
                    else:
                        s_div += ht.para(ht.add_tag("span", "frinx-implemented-writer",
                                                    {"class": "statement-info-label"}) + ":<br />" +
                                         "MISSING WRITER",
                                         {"class": "statement-info-text frinx-margin-left-medium"}, level, True)
                    s_div += ht.close_tag(newline=True)
                s_div += ht.close_tag(newline=True)
        # add link for others
        elif statement.attrs.has_key('frinx-usecase'):
            s_div += ht.para(
                ht.add_tag("span", "related usecases", {"class": "statement-info-label"}) + ": "
                + self.create_links(statement.attrs['frinx-usecase']), {"class": "statement-info-text"}, level, True)

        # check for additional properties
        notes = ""
        if statement.attrs['is_key']:
            notes += " (list key)"
        if statement.attrs['config']:
            notes += " (rw)"
        else:
            notes += " (ro)"
        keyword = statement.keyword + notes
        s_div += ht.para(ht.add_tag("span", "nodetype", {"class": "statement-info-label"}) + ": " + keyword,
                         {"class": "statement-info-text"}, level, True)

        # handle list nodes
        if statement.attrs['is_list']:
            list_keys = ""
            for key in statement.attrs['keys']:
                list_keys += " [" + ht.add_tag("a", key[0], {"href": "#" + key[1]}) + "]"
            s_div += ht.para(ht.add_tag("span", "list keys", {"class": "statement-info-label"}) + ": " + list_keys,
                             {"class": "statement-info-text"}, level, True)

        if statement.typedoc:
            s_div += gen_type_info(statement.typedoc, level)

        for prop in YangDocDefs.type_leaf_properties:
            if statement.attrs.has_key(prop):
                s_div += ht.para(
                    ht.add_tag("span", prop, {"class": "statement-info-label"}) + ": " + statement.attrs[prop],
                    {"class": "statement-info-text"}, level, True)

        # add this statement to the collection of data
        #self.moduledocs[statement.module_doc.module_name]['data'] += s_div
        s_r.print_content()

    def emitDocs(self, ctx, section=None):
        """Return the HTML output for all modules,
        or single section if specified"""

        ht = html_helper.HTMLHelper()

        r_docs = []
        docs = []
        navs = []
        navids = []
        # create the documentation elements for each module
        for module_name in self.moduledocs:
            # check if the module has no data nodes
            if 'data' not in self.moduledocs[module_name]:
                self.moduledocs[module_name]['data'] = ""
            else:
                # create the header for the data elements
                hdr = ht.h3("Data elements", {"class": "module-types-header", "id": module_name + "-data"}, 2, True)
                self.moduledocs[module_name]['data'] = hdr + self.moduledocs[module_name]['data']

            if section is not None:
                return self.moduledocs[module_name][section]
            else:
                r_docs.append(self.moduledocs[module_name]['data'])
                docs.append(self.moduledocs[module_name]['module'] +
                            self.moduledocs[module_name]['typedefs'] +
                            self.moduledocs[module_name]['identities'] +
                            self.moduledocs[module_name]['data'])
                navs.append(self.moduledocs[module_name]['navlist'])
                navids.append(self.moduledocs[module_name]['navid'])

        #if ctx.opts.doc_title is None:
            # just use the name of the first module returned by the dict if no title
            # is supplied
        #    doc_title = self.moduledocs.iterkeys().next()
        #else:
        doc_title = "bla"

        s = populate_template(doc_title, docs, navs, navids)
        #return r_docs
        return s


def gen_type_info(typedoc, level=1):
    """Create and return documentation based on the type.  Expands compound
    types."""

    ht = html_helper.HTMLHelper()
    s = ""

    # emit type-specific attributes
    typename = typedoc.typename
    s += ht.para(ht.add_tag("span", "type", {"class": "statement-info-label"}) + ": " + typename,
                 {"class": "statement-info-text"}, level, True)

    if typename == 'enumeration':
        s += " " * level + "<ul>\n"
        for (enum, desc) in typedoc.attrs['enums'].iteritems():
            s += " " * level + "<li>" + enum + "<br />" + desc + "</li>\n"
        s += " " * level + "</ul>\n"
    elif typename == 'string':
        if typedoc.attrs['restrictions'].has_key('pattern'):
            s += " " * level + "<ul>\n"
            s += " " * level + "<li>pattern:<br>\n"
            s += " " * level + typedoc.attrs['restrictions']['pattern'] + "\n</li>\n"
            s += " " * level + "</ul>\n"
    elif typename in YangDocDefs.integer_types:
        if typedoc.attrs['restrictions'].has_key('range'):
            s += " " * level + "<ul>\n"
            s += " " * level + "<li>range:\n"
            s += " " * level + typedoc.attrs['restrictions']['range'] + "\n</li>\n"
            s += " " * level + "</ul>\n"
    elif typename == 'identityref':
        s += " " * level + "<ul>\n"
        s += " " * level + "<li>base: " + typedoc.attrs['base'] + "</li>\n"
        s += " " * level + "</ul>\n"
    elif typename == 'leafref':
        s += " " * level + "<ul>\n"
        s += " " * level + "<li>path reference: " + typedoc.attrs['leafref_path'] + "</li>\n"
        s += " " * level + "</ul>\n"
    elif typename == 'union':
        s += " " * level + "<ul>\n"
        for childtype in typedoc.childtypes:
            s += " " * level + gen_type_info(childtype)
        s += " " * level + "</ul>\n"
    else:
        pass

    return s


def populate_template(title, docs, navs, nav_ids):
    """Populate HTML templates with the documentation content"""

    template_path = os.path.dirname(__file__) + "/../templates/yangdoc"
    j2_env = Environment(loader=FileSystemLoader(template_path),
                         trim_blocks=True)
    template = j2_env.get_template('yangdoc.html')

    return template.render({'title': title,
                            'htmldocs': docs,
                            'menus': navs,
                            'menu_ids': nav_ids})


def gen_nav_tree(emitter, root_mod, level=0):
    """Generate a list structure to serve as navigation for the
    module.  root_mod is a top-level ModuleDoc object"""

    ht = html_helper.HTMLHelper()

    # dont show elements in navigation that has no information for us
    if len(root_mod.module.children) <= 0:
        nav = "<ul id=\"%s\">\n" % ("tree-" + ht.gen_html_id(root_mod.module_name))
        nav += "</ul>\n"
        emitter.moduledocs[root_mod.module_name]['navlist'] = nav
        emitter.moduledocs[root_mod.module_name]['navid'] = "tree-" + ht.gen_html_id(root_mod.module_name)
        return
    nav = "<ul id=\"%s\">\n" % ("tree-" + ht.gen_html_id(root_mod.module_name))

    # module link
    if is_augmented(root_mod.module):
        nav += "<li><a class=\"menu-module-name, frinx-nav\" href=\"%s\">%s</a>\n" % (
            "#mod-" + ht.gen_html_id(root_mod.module_name), root_mod.module_name)
    else:
        nav += "<li><a class=\"menu-module-name\" href=\"%s\">%s</a>\n" % (
            "#mod-" + ht.gen_html_id(root_mod.module_name), root_mod.module_name)

    nav += "<ul>\n"
    # generate links for types and identities
    if len(root_mod.typedefs) > 0:
        nav += "<li><a href=\"%s\">%s</a>\n" % (
            "#" + ht.gen_html_id(root_mod.module_name) + "-defined-types", "Defined types")
        types = root_mod.typedefs.keys()
        nav += " <ul>\n"
        for typename in types:
            nav += "  <li><a href=\"%s\">%s</a></li>\n" % ("#type-" + ht.gen_html_id(typename), typename)
        nav += " </ul>\n"
        nav += "</li>\n"

    if len(root_mod.identities) > 0:
        nav += "<li><a href=\"%s\">%s</a>\n" % (
            "#" + ht.gen_html_id(root_mod.module_name) + "-identities", "Identities")
        nav += " <ul>\n"
        for base_id in root_mod.base_identities:
            derived = {key: value for key, value in root_mod.identities.items() if value.attrs['base'] == base_id}
            nav += "  <li><a href=\"%s\">%s</a>\n" % ("#ident-" + ht.gen_html_id(base_id), base_id)
            nav += "  <ul>\n"
            for idname in derived.keys():
                nav += "    <li><a href=\"%s\">%s</a></li>\n" % ("#ident-" + ht.gen_html_id(idname), idname)
            nav += "  </ul>\n"
            nav += "  </li>\n"
        nav += " </ul>\n"
        nav += "</li>\n"

    # generate links for data nodes
    top = root_mod.module
    level = 0
    if len(top.children) > 0:
        if is_augmented(root_mod.module):
            nav += "<li><a class=\"frinx-nav\" href=\"#%s-data\">%s</a>\n" % (root_mod.module_name, "Data elements")
        else:
            nav += "<li><a href=\"#%s-data\">%s</a>\n" % (root_mod.module_name, "Data elements")
        nav += "<ul>\n"
        for child in top.children:
            nav += gen_nav(child, root_mod, level)
        nav += "</li>\n"
        nav += "</ul>\n"

    nav += "</ul>\n"
    nav += "</ul>\n"
    nav += "</li>\n"

    # store the navigation list
    emitter.moduledocs[root_mod.module_name]['navlist'] = nav
    emitter.moduledocs[root_mod.module_name]['navid'] = "tree-" + ht.gen_html_id(root_mod.module_name)


def gen_nav(node, root_mod, level=0):
    """Add the list item for node (StatementDoc object)"""

    # print "nav: %s %s (%d)" % (node.keyword, node.name, len(node.children))
    current_level = level
    nav = ""
    if len(node.children) > 0:
        # print the current node (opening li element)

        if is_augmented(node):
            nav += " " * level + " <li>" + "<a class=\"frinx-nav\" href=\"#" + node.attrs[
                'id'] + "\">" + node.name + "</a>\n"
        else:
            nav += " " * level + " <li>" + "<a href=\"#" + node.attrs['id'] + "\">" + node.name + "</a>\n"
        # start new list for the children
        nav += " " * level + " <ul>\n"
        level += 1
        for child in node.children:
            nav += gen_nav(child, root_mod, level)
        # close list of children
        nav += " " * current_level + " </ul>\n"
        nav += " " * current_level + "</li>\n"
    else:
        # no children -- just print the current node and return
        if node.attrs.has_key('frinx-documentation') or node.attrs.has_key('frinx-usecase'):
            nav += " " * current_level + " <li>" "<a class=\"frinx-nav\" href=\"#" + node.attrs[
                'id'] + "\">" + node.name + "</a>\n"
        else:
            nav += " " * current_level + " <li>" "<a class=\"last-child-color\" href=\"#" + node.attrs[
                'id'] + "\">" + node.name + "</a>\n"

    return nav


def text_to_paragraphs(textblock):
    """Simple conversion of text into paragraphs based (naively) on blank
    lines -- intended to use with long, multi-paragraph descriptions"""

    paras = textblock.split("\n\n")
    return paras


def is_augmented(node):
    """Checks if the node tree is augmented by frinx augments for highlighing"""

    if node.attrs.has_key('frinx-documentation'):
        return True

    if len(node.children) > 0:
        for child in node.children:
            if is_augmented(child):
                return True
    return False


def r_heading_level(self, text, level):
    if level == 1:
        self.heading(text, char='=')
    if level == 2:
        self.heading(text, char='-')
    if level == 3:
        self.heading(text, char='~')
    if level == 4:
        self.heading(text, char='+')
    if level == 5:
        self.heading(text, char='^')
    if level == 6:
        self.heading(text, char=';')
    if level == 7:
        self.heading(text, char='^')
    if level == 8:
        self.heading(text, char='_')
    if level == 9:
        self.heading(text, char='*')
    if level == 10:
        self.heading(text, char='#')
    if level == 11:
        self.heading(text, char='$')
    if level == 12:
        self.heading(text, char='%')
    if level == 13:
        self.heading(text, char='&')
    if level == 14:
        self.heading(text, char='(')
    if level == 15:
        self.heading(text, char=')')
    if level == 16:
        self.heading(text, char=',')
    if level == 17:
        self.heading(text, char='/')
    if level == 18:
        self.heading(text, char=':')
    if level == 19:
        self.heading(text, char='<')
    if level == 20:
        self.heading(text, char='>')
    if level == 21:
        self.heading(text, char='?')
    if level == 22:
        self.heading(text, char='@')
    if level == 23:
        self.heading(text, char='[')
    if level == 24:
        self.heading(text, char=']')
    if level == 25:
        self.heading(text, char='"')
    if level == 26:
        self.heading(text, char='{')
    if level == 27:
        self.heading(text, char='|')
    if level == 28:
        self.heading(text, char='}')
    if level == 29:
        self.heading(text, char='.')
