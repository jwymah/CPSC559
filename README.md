#CPSC 559 - Distributed Systems Group Project#

[![Build
Status](https://travis-ci.org/cjhutchi/CPSC559.svg?branch=master)](https://travis-ci.org/cjhutchi/CPSC559)

An implementation of a Peer-to-Peer group messaging system for CPSC 559.
Each node runs as an individual client/server and can communicate over a local
LAN network. Discovery messages are broadcasted over UDP Multicast while
standard communication takes place over a TCP socket. This is primarily backend
work with a minimal user interfacing command line.

**Building:**
* Cloning: `git clone https://github.com/cjhutchi/CPSC559.git`
* Compile: `cd CPSC559` then run `ant` in the source directory
* Running: `cd CPSC559` then run `ant run` in the source directory
* Running (alternate): `java -classpath libs/*:deploy/libs/* com.github.group.P2PChat` in the source directory

For a list of available methods available while the program is running run
`/?<enter>` from the window.

**Contributors:**
* Cory Huthison
* Jeremy Mah
* Frankie Yuan

**Dependencies:**
* java 8
* json-simple-1.1.1
* junit-4.12
* hamcrest-all-1.1
