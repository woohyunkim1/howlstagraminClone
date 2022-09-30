from flask import Flask, request
from flask_restful import Resource, Api, request, abort

#Flask 인스턴스 정리
#Flask 객체 생성, flask_resful의 메인 기본 진입점 설정, Flask 객체로 초기화
app = Flask(__name__)
api = Api(app)


class HelloWorld(Resource):
    def get(self):
        return {'hello': 'world'}

api.add_resource(HelloWorld, '/')

todos = {}
count = 1

class TodoPost(Resource):
	def get(self):
		return todos
	def post(self):
		global count
		global todos

		idx = count
		count += 1
		todos[idx] = 'success'
		return {
			'todo_id': str(idx),
			'data': todos[idx]
		}
api.add_resource(TodoPost, '/todos')


class TodoList(Resource):
	 	#get은 딕셔너리에서 원하는 인덱스의 내용이 반환
		def get(self, todo_id):
			return {todo_id: todos[todo_id]}

		# put은 데이터를 삽입하고 내용을 반환
		def put(self, todo_id):
			todos[todo_id] = 'change'
			return {todo_id: todos[todo_id]}

		# delete는 내용을 지우고 status를 204로 반환
		def delete(self, todo_id):
			del todos[todo_id]
			return '', 204

# api에 대한 리소스 클래스 추가하고, 라우터 설정
api.add_resource(TodoList, '/todos/<int:todo_id>')


if __name__ == '__main__':
	app.run(host="0.0.0.0", port =5555)
