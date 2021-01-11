from flask import Flask, render_template, jsonify
import subprocess
import json

server = Flask(__name__)

@server.route("/")
def homepage():
    return render_template("index.html")

@server.route("/scramble", methods=["GET"])
def scramble():
    out = subprocess.run("java -jar rubikSolver.jar 4", shell=True, stdout=subprocess.PIPE).stdout.decode()
    out = out.split("\n")[-3:-1]
    out = {"scramble":out[0], "state":out[1]}
    return jsonify(out)