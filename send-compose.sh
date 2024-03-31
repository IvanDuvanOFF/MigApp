#!/bin/bash

scp -i ./key.pub ./README.md root@"$SERVER_HOST":/app/mig-api/README.md