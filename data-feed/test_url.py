import requests
url = 'http://127.0.0.1:8080/deep-qa-web-1.2/api/ask'
question = "how are you"
payload = {'n': '10', 'q': question}
r = requests.post(url, data=payload)
print r.text
