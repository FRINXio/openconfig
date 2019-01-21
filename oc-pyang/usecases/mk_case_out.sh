echo "module frinx-openconfig-uc-$1 {
  yang-version \"1\";
  namespace \"http://frinx.openconfig.net/yang/openconfig-uc-$1:docs\";
  prefix \"oc-if-docs-uc\";

  import frinx-openconfig-interfaces { prefix oc-if; }
  import frinx-openconfig-if-ip { prefix oc-ip; }
  import frinx-openconfig-if-ethernet { prefix oc-eth; }
  import frinx-openconfig-acl { prefix oc-acl; }
  import frinx-acl-extension { prefix oc-acl-ext; }
  import frinx-openconfig-vlan { prefix oc-vlan; }
  import frinx-lacp-lag-member { prefix lacp-lag-mem; }
  import frinx-openconfig-if-aggregate{ prefix oc-lag; }
  import frinx-cisco-if-extension { prefix cisco-if-ext; }
  import frinx-damping { prefix damp; }

  description \"$2\";
  reference \"http://www.frinx.io\";

  extension frinx-usecase {
   argument \"name\";
  }" > "frinx-openconfig-uc-$1.yang"

if [ "$3" ] 
  then echo "
  augment \"$3\" {
   container usecase-tag {
    oc-if-docs-uc:frinx-usecase \"$1\";
   }
  }" >> "frinx-openconfig-uc-$1.yang"
fi
if [ "$4" ]
  then echo "
  augment \"$4\" {
   container usecase-tag {
    oc-if-docs-uc:frinx-usecase \"$1\";
   }
  }" >> "frinx-openconfig-uc-$1.yang"
fi
if [ "$5" ]
  then echo "
  augment \"$5\" {
   container usecase-tag {
    oc-if-docs-uc:frinx-usecase \"$1\";
   }
  }" >> "frinx-openconfig-uc-$1.yang"
fi
if [ "$6" ]
  then echo "
  augment \"$6\" {
   container usecase-tag {
    oc-if-docs-uc:frinx-usecase \"$1\";
   }
  }" >> "frinx-openconfig-uc-$1.yang"
fi
if [ "$7" ]
  then echo "
  augment \"$7\" {
   container usecase-tag {
    oc-if-docs-uc:frinx-usecase \"$1\";
   }
  }" >> "frinx-openconfig-uc-$1.yang"
fi
if [ "$8" ]
  then echo "
  augment \"$8\" {
   container usecase-tag {
    oc-if-docs-uc:frinx-usecase \"$1\";
   }
  }" >> "frinx-openconfig-uc-$1.yang"
fi
if [ "$9" ]
  then echo "
  augment \"$9\" {
   container usecase-tag {
    oc-if-docs-uc:frinx-usecase \"$1\";
   }
  }" >> "frinx-openconfig-uc-$1.yang"
fi

echo "}" >> "frinx-openconfig-uc-$1.yang"
