#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $DIR

# resolved_yangs.txt contains list of all YANG models ordered based on their imports
# -W none ignore WARNINGS
pyang -f jstree $(cat resolved_yangs.txt) -W none > index.html