#mkdir other
#cd other
#wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/oxygen/model/ietf/ietf-topology/src/main/yang/network-topology%402013-10-21.yang -O network-topology@2013-10-21.yang
#wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/oxygen/model/yang-ext/src/main/yang/yang-ext.yang -O yang-ext.yang
#wget https://raw.githubusercontent.com/YangModels/yang/master/vendor/cisco/xr/534/ietf-inet-types.yang
#wget https://raw.githubusercontent.com/YangModels/yang/master/vendor/cisco/xe/1681/ietf-yang-types.yang
#wget https://raw.githubusercontent.com/YangModels/yang/master/experimental/odp/iana-if-type.yang
#wget https://raw.githubusercontent.com/YangModels/yang/master/vendor/cisco/nx/7.0-3-I5-1/ietf-interfaces.yang
#cd ..

#mkdir venv
virtualenv -p /usr/bin/python2.7 venv/yang-docs-generator
cd venv/yang-docs-generator/bin
source ./activate
#pip install pyang
#pip install ninja2
#pip install flask
#pip install enum34
#pip install rstcloth
#cd ../../..

rm ~/extend/code/openconfig/oc-pyang/frinx-yang/*

cd ~/extend/code/cli-units/
find . -wholename */META-INF/yang/frinx-openconfig-*.yang -exec bash -c 'cp "$1" ~/extend/code/openconfig/oc-pyang/frinx-yang' sh {} \;

cd ~/extend/code/unitopo-units/
find . -wholename */META-INF/yang/frinx-openconfig-*.yang -exec bash -c 'cp "$1" ~/extend/code/openconfig/oc-pyang/frinx-yang' sh {} \;

cd ~/extend/code/openconfig/
find . -wholename */META-INF/yang/*.yang -exec bash -c 'cp "$1" ~/extend/code/openconfig/oc-pyang/frinx-yang' sh {} \;

cd ~/extend/code/openconfig/oc-pyang/
./venv/yang-docs-generator/bin/python2.7 yangSequenceGeneration.py
./venv/yang-docs-generator/bin/pyang -f docs --doc-format=html --path=./other --plugindir ./openconfig_pyang/plugins/ $(cat frinx_yangs.txt) > 4.2.4.rc23-frinx-SNAPSHOT.html