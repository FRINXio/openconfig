""""
Copyright 2018 Frinx.io, s.r.o

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


Implements an function to generate all implemented nodes in FRINX ODL for each device respectively..

"""
import os
import re

from jinja2 import Environment, FileSystemLoader
from doc_emitter import DocEmitter

END_LINE = "\n"
BASE_URL = "https://frinxio.github.io/translation-units-docs/v3.1.6.frinx/3.1.6.frinx.html"
DIR = "devices"


class DeviceEmitter(DocEmitter):

    def genModuleDoc(self, mod, ctx):
        #   NOT NEDDED -- NOOP
        return

    def genStatementDoc(self, statement, ctx, level=1):
        """PATH emitter for frinx-documentation module data node given a StatementDoc
        object.
        Collect all elements that are implemented by Frinx and adds them into layered tree like structure"""

        if ctx.opts.no_structure and statement.keyword in ctx.skip_keywords:
            return

        if statement.attrs.has_key('frinx-documentation'):
            prefixes = find_frinx_prefixes(statement.attrs['frinx-documentation'].keys())
            fixed_prefix = ""
            for prefix in prefixes:
                if statement.attrs['frinx-documentation'].has_key(prefix):
                    self.parse_and_add_path(prefix, statement.attrs['path'])
                    fixed_prefix = prefix
            self.moduledocs[prefix]['device-name'] = statement.attrs['frinx-documentation'][fixed_prefix][
                'frinx-docs-type']
            self.moduledocs[prefix]['device-version'] = statement.attrs['frinx-documentation'][fixed_prefix][
                'frinx-docs-version']

    def emitDocs(self, ctx, section=None):
        """Creates files for each frinx prefix we have found. Returns list of all prefixes"""

        create_folder(DIR)
        # Creates files for each prefix we have found.
        for prefix in self.moduledocs.keys():
            docs = recursive_structure_builder(self.moduledocs[prefix]['nodes'])
            name = self.moduledocs[prefix]['device-name']
            version = self.moduledocs[prefix]['device-version']
            dst_path_with_filename = os.path.join(DIR, prefix + ".html")
            output_file = open(dst_path_with_filename, "w")
            output_file.write(populate_template(name, docs, version))
            output_file.close()
        # As we already create all needed files we return atleast list of all prefixes used.
        # Which can be used in jenkins to help automate website building.
        return '\n'.join(self.moduledocs.keys())


    def parse_and_add_path(self, prefix, path):
        """Creates an recursive dictionary data structure where all paths are stored"""
        if prefix not in self.moduledocs.keys():
            self.moduledocs[prefix] = {}
        if 'nodes' not in self.moduledocs[prefix].keys():
            self.moduledocs[prefix]['nodes'] = {}
        list_of_elements = path.split('/')
        current_sub_structure = self.moduledocs[prefix]['nodes']
        for element in list_of_elements[1:]:
            if element not in current_sub_structure:
                current_sub_structure[element] = {}
            current_sub_structure = current_sub_structure[element]


def recursive_structure_builder(data_structure, level=0, label=""):
    output = ""
    if not bool(data_structure):
        return output
    keys = data_structure.keys()
    keys = sorted(keys, key=compare_without_prefix)
    output += "<ol>"
    for idx, key in enumerate(keys):
        if level == 0:
            new_label = keys[idx].split(":")[1]
            output += "<li>" + create_link(keys[idx], new_label) + "</li>" + END_LINE
        else:
            new_label = label + "-" + keys[idx].split(":")[1]
            output += "<li>" + create_link(keys[idx].split(":")[1], new_label) + "</li>" + END_LINE
        output += recursive_structure_builder(data_structure[keys[idx]], level + 1, new_label)
    output += "</ol>"
    return output


def compare_without_prefix(element):
    return element.split(":")[1]


def create_link(name, label):
    link = "<a href=" + BASE_URL + "#" + label + '>' + name + '</a>'
    return link


def find_frinx_prefixes(all_keys):
    regex = re.compile(r'^frinx-oc-.*-docs$')

    return filter(regex.search, all_keys)


def populate_template(device_name, docs, device_version):
    """Populate HTML templates with the documentation content"""

    template_path = os.path.dirname(__file__) + "/../templates/devicedoc"
    j2_env = Environment(loader=FileSystemLoader(template_path),
                         trim_blocks=True)
    template = j2_env.get_template('devicedoc.html')

    return template.render({'name': device_name,
                            'version': device_version,
                            'htmldocs': docs})


def create_folder(directory):
    try:
        if not os.path.exists(directory):
            os.makedirs(directory)
    except OSError:
        print ('Error: Creating directory. ' + directory)
