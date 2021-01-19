import sys
from flask import Flask, render_template, jsonify, request
import subprocess
import json
import pathlib

server = Flask(__name__)
PATH = str(pathlib.Path(__file__).parent.absolute()) + "/"

@server.route("/")
def homepage():
    return render_template("index.html")

@server.route("/scramble", methods=["GET"])
def scramble():
    out = subprocess.run(f"java -jar {PATH}rubikSolver.jar 4", shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    out = out.stdout.decode()
    out = out.split("\n")[-3:-1]
    out = {"scramble":out[0], "state":out[1]}
    return jsonify(out)

@server.route("/solve", methods=["GET"])
def solve(): #TODO: aggiungere la possibilità di azionare i motori del robottone dal sito
    state = request.args.get("cube")
    solve = subprocess.run(f"java -jar {PATH}rubikSolver.jar 1 {state}", shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE).stdout.decode()
    solve = solve.split("#")[-1]
    return jsonify({"solve": solve})

if __name__ == "__main__":
    server.run(debug=True, use_debugger=False, use_reloader=True)