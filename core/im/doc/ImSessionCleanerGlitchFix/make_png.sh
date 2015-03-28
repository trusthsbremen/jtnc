#!/bin/bash
dot -Timap -oDefaultImSessionManager.Clean.map DefaultImSessionManager.Clean.dot
dot -Tpng -oDefaultImSessionManager.Clean.png DefaultImSessionManager.Clean.dot 
