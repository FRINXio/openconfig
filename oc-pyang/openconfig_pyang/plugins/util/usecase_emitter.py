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

"""
import re

from doc_emitter import DocEmitter


class UsecaseEmitter(DocEmitter):
    """This emitter is used to generate CSV file with implemented usecase scenarios.
    Use mk_case_out.sh to generate coresponding .Yang file whick should used as input into this plugin.
    Don't forget to use usecase as format parameter"""

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
            for prefix in prefixes:
                if statement.attrs['frinx-documentation'].has_key(prefix):
                    self.parse_and_add_path(prefix, statement.attrs['path'])

        if statement.attrs.has_key('frinx-usecase'):
            self.parse_and_add_path_for_usecase(statement.attrs['frinx-usecase'], statement.attrs['path'])

    def emitDocs(self, ctx, section=None):
        """Creates files for each frinx prefix we have found. Returns list of all prefixes"""
        prefixes = self.moduledocs.keys()
        prefixes.remove('usecases')
        csv_output = ""
        all_use_cases = self.moduledocs['usecases']
        sorted_use_cases = sorted(all_use_cases)
        short_prefixes = []
        for pref in prefixes:
            tmp = pref.split('-')
            short_prefixes.append('-'.join(tmp[2:-1]))
        for use_case in sorted_use_cases:
            csv_output += use_case + "," + ",".join(short_prefixes) + '\n'
            for path in self.moduledocs['usecases'][use_case]:
                csv_output += path + ","
                for prefix in prefixes:
                    if not self.is_path_in_structure(prefix, path):
                        csv_output += "0,"
                    else:
                        csv_output += "1,"
                csv_output += "\n"
        return csv_output

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

    def parse_and_add_path_for_usecase(self, usecases, path):
        """Creates an recursive dictionary data structure where all paths are stored"""
        if 'usecases' not in self.moduledocs.keys():
            self.moduledocs['usecases'] = {}
        for usecase in usecases:
            if usecase not in self.moduledocs['usecases'].keys():
                self.moduledocs['usecases'][usecase] = []
            self.moduledocs['usecases'][usecase].append(path)

    def is_path_in_structure(self, prefix, path):
        list_of_elements = path.split('/')
        current_sub_structure = self.moduledocs[prefix]['nodes']
        for element in list_of_elements[1:-1]:
            if element not in current_sub_structure:
                return False
            current_sub_structure = current_sub_structure[element]
        return True


def find_frinx_prefixes(all_keys):
    regex = re.compile(r'^frinx-oc-.*-docs$')

    return filter(regex.search, all_keys)
