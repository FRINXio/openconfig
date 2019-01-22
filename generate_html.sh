#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $DIR

# download dependencies
wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/carbon/model/ietf/ietf-yang-types-20130715/src/main/yang/ietf-yang-types%402013-07-15.yang -O ietf-yang-types@2013-07-15.yang
wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/carbon/model/ietf/ietf-interfaces/src/main/yang/ietf-interfaces.yang -O ietf-interfaces.yang
wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/carbon/model/iana/iana-if-type-2014-05-08/src/main/yang/iana-if-type%402014-05-08.yang -O iana-if-type@2014-05-08.yang
wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/carbon/model/ietf/ietf-inet-types-2013-07-15/src/main/yang/ietf-inet-types%402013-07-15.yang -O ietf-inet-types@2013-07-15.yang
wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/carbon/model/ietf/ietf-topology/src/main/yang/network-topology%402013-10-21.yang -O network-topology@2013-10-21.yang
wget https://raw.githubusercontent.com/opendaylight/mdsal/stable/carbon/model/yang-ext/src/main/yang/yang-ext.yang -O yang-ext.yang

# resolved_yangs.txt contains list of all YANG models ordered based on their imports
# -W none ignore WARNINGS
pyang -f jstree ietf-yang-types@2013-07-15.yang ietf-interfaces.yang iana-if-type@2014-05-08.yang ietf-inet-types@2013-07-15.yang network-topology@2013-10-21.yang yang-ext.yang $(cat resolved_yangs.txt) -W none > index.html

# remove dependencies
rm ietf-yang-types@2013-07-15.yang
rm ietf-interfaces.yang
rm ietf-inet-types@2013-07-15.yang
rm iana-if-type@2014-05-08.yang
rm network-topology@2013-10-21.yang
rm yang-ext.yang