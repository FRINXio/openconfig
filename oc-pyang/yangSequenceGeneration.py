import glob
import re

directory = '/home/*/extend/code/openconfig/oc-pyang/frinx-yang/'
name = "frinx_yangs.txt"
moduleNameList = []
name_importDict = {}
moduleCount = 0
for yangFile in glob.glob(directory + "*.yang"):
    noValue = True
    for searchLine in open(yangFile):
        comment = '//'
        badInclude = 'include ^'
        if (comment in searchLine) or (badInclude in searchLine):
            continue
        else:
            moduleName = re.search(r'module (\S+) {', searchLine)
            if moduleName is not None:
                moduleNameList.append(moduleName.groups()[0])

            importModule = re.search(r'import (\S+) {', searchLine)
            if importModule is not None:
                name_importDict.setdefault(moduleNameList[moduleCount], []).append(importModule.groups()[0])
                noValue = False

            includeModule = re.search(r'include (\S+) {', searchLine)
            if includeModule is not None:
                name_importDict.setdefault(moduleNameList[moduleCount], []).append(includeModule.groups()[0])
                noValue = False
    if noValue:
        name_importDict.setdefault(moduleNameList[moduleCount], [])
    moduleCount += 1

outputListModule = []
otherYangModule = ['iana-if-type', 'ietf-inet-types', 'ietf-yang-types', 'ietf-interfaces',
                   'yang-ext', 'network-topology']
i = 0
shutDownLoop = 1
while i < (len(moduleNameList)):
    for key in name_importDict:
        if key not in outputListModule:
            if len(name_importDict.get(key)) == 0:
                outputListModule.append(key)
                outputListModule.append('\n')
                i += 1
                shutDownLoop = 0
            else:
                cantAdd = False
                for value in name_importDict.get(key):
                    if (value not in outputListModule) and (value not in otherYangModule):
                        cantAdd = True
                if not cantAdd:
                    outputListModule.append(key)
                    outputListModule.append('\n')
                    i += 1
                    shutDownLoop = 0
    shutDownLoop += 1
    if shutDownLoop == 3:
        break

for i in moduleNameList:
    if i not in outputListModule:
        print('Misses YANG models: ' + i + '\n')

text = ""
for module in outputListModule:
    for f in glob.iglob(directory + module + '@' + '*'):
        text += f + '\n'
    for f in glob.iglob(directory + module + '.' + '*'):
        text += f + '\n'

with open(name, "w") as text_file:
    for i in text:
        text_file.write("{0}".format(i))
