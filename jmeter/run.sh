#!/bin/bash

bash "$1" -n -t ./plans/TG1000-1-h.jmx -l ./logs/1000-1-h/log-1000-1-h.jtl -e -o ./res/1000-1-h
bash "$1" -n -t ./plans/TG1000-1-m.jmx -l ./logs/1000-1-m/log-1000-1-m.jtl -e -o ./res/1000-1-m
bash "$1" -n -t ./plans/TG1000-1-l.jmx -l ./logs/1000-1-l/log-1000-1-l.jtl -e -o ./res/1000-1-l