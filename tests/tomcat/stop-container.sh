#!/bin/bash

sudo docker kill `cat container.cid`
sudo rm container.cid
