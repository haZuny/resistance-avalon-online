from flask import Flask, Response
import time
import json

app = Flask(__name__)

def generate_events():
    count = 0
    while True:
        count += 1
        data = {
            'id': count,
            'message': f'This is message {count}',
            'timestamp': time.time()
        }
        yield f"data: {json.dumps(data)}\n\n"
        time.sleep(2)  # 2초마다 이벤트 전송

@app.route('/sse')
def sse():
    return Response(generate_events(), content_type='text/event-stream')

@app.route('/')
def home():
    return "Server is running. Connect to /sse for SSE events."

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)