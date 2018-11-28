import hashlib
import os
import sys

def which_python():
    if (sys.version_info > (3, 0)):
        return 3
    else:
        return 2

python_version = which_python()

def computeMD5hash(stringg):
    m = hashlib.md5()
    if python_version == 2:
        m.update(stringg.encode('utf8'))
    else:
        m.update(stringg)
    return m.hexdigest()

if not os.path.exists("md5/"):
    os.makedirs("md5/")

def get_content(filename):
    with open(filename, "rb") as f:
        return f.read()

for root,dirs,files in os.walk('.'):
    for file in files:
        if '.txt' in file:
            md5_string = computeMD5hash(get_content(file))
            f1 = open("md5/"+file, 'w')
            f1.write(md5_string)
            f1.close()