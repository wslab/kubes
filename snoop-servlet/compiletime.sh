#!/bin/bash
OUTPUTDIR=$1

COMPILETIME_FILE=${OUTPUTDIR}/compiletime.txt
GITBRANCH_FILE=${OUTPUTDIR}/gitbranch.txt
GITREV_FILE=${OUTPUTDIR}/gitrev.txt

if [ -z "$OUTPUTDIR" ]; then
    echo "Usage: compiletime outputdir"
    return 1
fi

DATE=`date`

echo ${DATE} > ${COMPILETIME_FILE}
echo wrote ${DATE} to ${COMPILETIME_FILE}

git rev-parse --abbrev-ref HEAD > ${GITBRANCH_FILE}
git rev-parse --short HEAD > ${GITREV_FILE}
