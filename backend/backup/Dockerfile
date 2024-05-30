FROM python:3.9.9-alpine

WORKDIR /
COPY . .

ENTRYPOINT ["python", "main.py", "-s", "/tmp/source", "-d", "/tmp/dist", "-b", "/tmp/back"]