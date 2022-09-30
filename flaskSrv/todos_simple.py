from flask import Flask
from flask_restful import Resource, Api, request, abort

#Flask 인스턴스 정리
#Flask 객체 생성, flask_resful의 메인 기본 진입점 설정, Flask 객체로 초기화
app = Flask(__name__)
api = Api(app)

Todos = {
    'todo1': {"task": "exercise"},
    'todo2': {'task': "eat delivery food"},
    'todo3': {'task': 'watch movie'}
}

class TodoList(Resource):
    def get(self):
        return Todos


api.add_resource(TodoList, '/todos/')

if __name__ == '__main__':
	app.run(host="localhost", port =5000, debug=True)
