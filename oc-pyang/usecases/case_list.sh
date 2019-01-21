./mk_case_out.sh if-name "\
Configure interface name" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name

./mk_case_out.sh if-desc "\
Configure interface description" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:description

./mk_case_out.sh if-enabled "\
Configure interface administrative state (enabled / disabled)" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:enabled

./mk_case_out.sh if-type "\
Configure interface type" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type

./mk_case_out.sh if-mtu "\
Configure interface mtu" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:mtu

./mk_case_out.sh if-tpid "\
Configure interface tpid" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-vlan:tpid

./mk_case_out.sh if-hold-time "\
Configure interface hold-time" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:hold-time/oc-if:config/oc-if:up \
/oc-if:interfaces/oc-if:interface/oc-if:hold-time/oc-if:config/oc-if:down

./mk_case_out.sh if-subif-index "\
Configure subinterface index" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:index

./mk_case_out.sh if-subif-name "\
Configure subinterface name" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:index \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:name

./mk_case_out.sh if-subif-desc "\
Configure subinterface description" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:index \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:description

./mk_case_out.sh if-subif-enabled "\
Configure subinterface administrative state (enabled / disabled)" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:index \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:enabled

./mk_case_out.sh if-subif-vlan "\
Configure subinterface dot1q vlan" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:index \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-vlan:vlan/oc-vlan:config/oc-vlan:vlan-id

./mk_case_out.sh if-subif-addr-ipv4 "\
Configure interface/subinterface ipv4 address" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:index \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-ip:ipv4/oc-ip:addresses/oc-ip:address/oc-ip:config/oc-ip:ip \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-ip:ipv4/oc-ip:addresses/oc-ip:address/oc-ip:config/oc-ip:prefix-length

./mk_case_out.sh if-subif-addr-ipv6 "\
Configure interface/subinterface ipv6 address" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:index \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-ip:ipv6/oc-ip:addresses/oc-ip:address/oc-ip:config/oc-ip:ip \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-ip:ipv6/oc-ip:addresses/oc-ip:address/oc-ip:config/oc-ip:prefix-length

./mk_case_out.sh if-subif-ra-suppress "\
Configure interface/subinterface ipv6 neighbor discovery advertisement suppression" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-if:config/oc-if:index \
/oc-if:interfaces/oc-if:interface/oc-if:subinterfaces/oc-if:subinterface/oc-ip:ipv6/oc-ip:router-advertisement/oc-ip:config/oc-ip:suppress

./mk_case_out.sh if-damping "\
Configure interface damping" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/damp:damping/damp:config/damp:enabled \
/oc-if:interfaces/oc-if:interface/damp:damping/damp:config/damp:half-life \
/oc-if:interfaces/oc-if:interface/damp:damping/damp:config/damp:reuse \
/oc-if:interfaces/oc-if:interface/damp:damping/damp:config/damp:suppress \
/oc-if:interfaces/oc-if:interface/damp:damping/damp:config/damp:max-suppress

./mk_case_out.sh if-bundle-id "\
Configure interface aggregate bundle id" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-eth:ethernet/oc-eth:config/oc-lag:aggregate-id

./mk_case_out.sh if-lacp-mode "\
Configure interface LACP mode" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-eth:ethernet/oc-eth:config/lacp-lag-mem:lacp-mode

./mk_case_out.sh if-lacp-interval "\
Configure interface LACP interval" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-eth:ethernet/oc-eth:config/lacp-lag-mem:interval

# ./mk_case_out.sh if-lacp-key "\
# Configure interface LACP admin key !!!NOT IMPLEMENTED and wrong naming" \
# /oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
# /oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
# /oc-if:interfaces/oc-if:interface/oc-eth:ethernet/oc-eth:config/oc-eth:admin-key

./mk_case_out.sh if-l2-mode "\
Configure L2 interface mode" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-eth:ethernet/oc-vlan:switched-vlan/oc-vlan:config/oc-vlan:interface-mode

./mk_case_out.sh if-l2-native-vlan "\
Configure L2 interface native vlan" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-eth:ethernet/oc-vlan:switched-vlan/oc-vlan:config/oc-vlan:native-vlan

./mk_case_out.sh if-l2-access-vlan "\
Configure L2 interface access vlan" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-eth:ethernet/oc-vlan:switched-vlan/oc-vlan:config/oc-vlan:access-vlan

./mk_case_out.sh if-l2-trunk-vlans "\
Configure L2 interface trunk vlans" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/oc-eth:ethernet/oc-vlan:switched-vlan/oc-vlan:config/oc-vlan:trunk-vlans

./mk_case_out.sh if-load-interval "\
Configure interface load statistics interval" \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:name \
/oc-if:interfaces/oc-if:interface/oc-if:config/oc-if:type \
/oc-if:interfaces/oc-if:interface/cisco-if-ext:statistics/cisco-if-ext:config/cisco-if-ext:load-interval

./mk_case_out.sh if-acl-ingress "\
Configure interface access list in inbound direction" \
/oc-acl:acl/oc-acl:interfaces/oc-acl:interface/oc-acl:config/oc-acl:id \
/oc-acl:acl/oc-acl:interfaces/oc-acl:interface/oc-acl:ingress-acl-sets/oc-acl:ingress-acl-set/oc-acl:config/oc-acl:set-name \
/oc-acl:acl/oc-acl:interfaces/oc-acl:interface/oc-acl:ingress-acl-sets/oc-acl:ingress-acl-set/oc-acl:config/oc-acl:type

./mk_case_out.sh if-acl-egress "\
Configure interface access list in outbound direction" \
/oc-acl:acl/oc-acl:interfaces/oc-acl:interface/oc-acl:config/oc-acl:id \
/oc-acl:acl/oc-acl:interfaces/oc-acl:interface/oc-acl:egress-acl-sets/oc-acl:egress-acl-set/oc-acl:set-name \
/oc-acl:acl/oc-acl:interfaces/oc-acl:interface/oc-acl:egress-acl-sets/oc-acl:egress-acl-set/oc-acl:type

./mk_case_out.sh acl "\
Configure access list" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:type \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:description

./mk_case_out.sh acl-ipv4-addr-src "\
Configure IPV4 access list entry - source address" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:source-address

./mk_case_out.sh acl-ipv4-addr-dst "\
Configure IPV4 access list entry - destination address" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:destination-address

./mk_case_out.sh acl-ipv4-dscp "\
Configure IPV4 access list entry - dscp" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:dscp

./mk_case_out.sh acl-ipv4-proto "\
Configure IPV4 access list entry - protocol" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:protocol

./mk_case_out.sh acl-ipv4-hop-range "\
Configure IPV4 access list entry - hop range" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:hop-range

./mk_case_out.sh acl-ipv4-hop-limit "\
Configure IPV4 access list entry - hop limit" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:hop-limit

./mk_case_out.sh acl-ipv4-addr-src-wc "\
Configure IPV4 access list entry - source address wildcarded" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:source-address-wildcarded/oc-acl-ext:address \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:source-address-wildcarded/oc-acl-ext:wildcard-mask

./mk_case_out.sh acl-ipv4-addr-dst-wc "\
Configure IPV4 access list entry - destination address wildcarded" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:destination-address-wildcarded/oc-acl-ext:address \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:destination-address-wildcarded/oc-acl-ext:wildcard-mask

./mk_case_out.sh acl-ipv6-addr-src "\
Configure IPV4 access list entry - source address" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:source-address

./mk_case_out.sh acl-ipv6-addr-dst "\
Configure IPV4 access list entry - destination address" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:destination-address

./mk_case_out.sh acl-ipv6-dscp "\
Configure IPV4 access list entry - dscp" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:dscp

./mk_case_out.sh acl-ipv6-proto "\
Configure IPV4 access list entry - protocol" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:protocol

./mk_case_out.sh acl-ipv6-hop-range "\
Configure IPV4 access list entry - hop range" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:hop-range

./mk_case_out.sh acl-ipv6-hop-limit "\
Configure IPV4 access list entry - hop limit" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl:hop-limit

./mk_case_out.sh acl-ipv6-addr-src-wc "\
Configure IPV4 access list entry - source address wildcarded" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:source-address-wildcarded/oc-acl-ext:address \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:source-address-wildcarded/oc-acl-ext:wildcard-mask

./mk_case_out.sh acl-ipv6-addr-dst-wc "\
Configure IPV4 access list entry - destination address wildcarded" \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:config/oc-acl:name \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:sequence-id \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:config/oc-acl:description \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:destination-address-wildcarded/oc-acl-ext:address \
/oc-acl:acl/oc-acl:acl-sets/oc-acl:acl-set/oc-acl:acl-entries/oc-acl:acl-entry/oc-acl:ipv4/oc-acl:config/oc-acl-ext:destination-address-wildcarded/oc-acl-ext:wildcard-mask

