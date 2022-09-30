from flask import Flask
from flask_restful import Resource, Api, request, abort

#Flask 인스턴스 정리
#Flask 객체 생성, flask_resful의 메인 기본 진입점 설정, Flask 객체로 초기화
app = Flask(__name__)
api = Api(app)

@app.route('/')
def hello_world():
    return 'Hello World!'
@app.route('/index')
def hello():
	return 'index Page'
@app.route('/user/<username>')
def show_user_profile(username):
	return 'User %s' %username
@app.route('/post/<int:post_id>')
def show_post(post_id):
	return 'Post %d' %post_id
@app.route('/projects')
def projects():
	return 'The project page'
@app.route('/about')
def about():
	return 'The about page'

if __name__ == '__main__':
	app.run(host="localhost", port =5000, debug=True)

