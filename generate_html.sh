#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $DIR

# download dependencies
wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/carbon/model/ietf/ietf-topology/src/main/yang/network-topology%402013-10-21.yang -O network-topology@2013-10-21.yang
wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/carbon/model/yang-ext/src/main/yang/yang-ext.yang -O yang-ext.yang

# resolved_yangs.txt contains list of all YANG models ordered based on their imports
# -W none ignore WARNINGS
pyang -f jstree network-topology@2013-10-21.yang yang-ext.yang $(cat resolved_yangs.txt) -W none > index.html

# remove dependencies
rm network-topology@2013-10-21.yang
rm yang-ext.yang