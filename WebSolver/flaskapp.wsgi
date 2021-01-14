#!/usr/bin/python
import sys
import logging
logging.basicConfig(stream=sys.stderr)
sys.path.insert(0,"/var/www/WebSolver/")

from WebSolver import server as application
application.secret_key = 'flask'
