# Use-cases
This directory is supposed to hold all template and scripts for generating a use-case .Yang files which are then entered into Pyang with oc-pyang plugin to generate CSV file with table corresponding to implementation of those use-cases.
## Common usage: 
	./case_list.sh 
	- to generate Yang, then 
	pyang -f docs --doc-format=usecase --plugindir PATH_TO_OC-PYANG/oc-pyang/openconfig_pyang/plugins/ {generated yangs in previous step} {frinx-yang models} {openconfig-yang-models} > {outputfile}
	
## Note
Don't forget to use *"usecase"* format on --docs-format switch.
"frinx-yang-models" represents models generated from Cli-units and Unitopo-units.
"openconfig-yang-models" are models in Openconfig project.
If you want to validate generated yang, please use tool [Pyang](https://github.com/mbj4668/pyang). Mind that you might need to add some Yang models to your Pyang search path.
	

