#!/bin/bash

REPO=localhost:32000
LABEL=dev

echo building image, tag: ${REPO}/snoop:${LABEL}
docker build -t ${REPO}/snoop:${LABEL} .

if [ $? -ne 0 ]
then
  echo docker build error, stop.
  return
fi

echo publishing to ${REPO}/snoop:${LABEL}
docker push ${REPO}/snoop:${LABEL}

if [ $? -ne 0 ]
then
  echo docker publish error, stop.
  return
fi

echo Done.
