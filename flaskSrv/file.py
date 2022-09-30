from flask import Flask #서버 구현을 위한 Flask 객체 import
from flask_restful import Resource, Api, request, abort, reqparse # Api 구현을 위한 Api 객체 import
from werkzeug.datastructures import FileStorage
from werkzeug.utils import secure_filename
import os


app = Flask(__name__) # Flask 객체 선언, 파라미터로 어플리케이션 패키지의 이름을 넣어줌
api = Api(app) # Flask 객체에 Api 객체 등록

ALLOWED_EXTENSIONS = ['jpg', 'jpeg', 'png', 'gif']

class UploadImage(Resource):
    def get(self):
        return{"status":"hide"}

    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('image', type=FileStorage, location='files', action='append')
        args = parser.parse_args()
        images = args['image']
        for image in images:
            extension = image.filename.split('.')[-1]
            if extension in ALLOWED_EXTENSIONS:
                image.save('./upload/{0}'.format(secure_filename(image.filename)))
            else:
                return {"status":"false", "result":"Not allowed extension"}
        return {"status":"true", "result":"Upload success"}

api.add_resource(UploadImage, '/image')


class UploadWaveApi(Resource):
    def post(selfself):
        parse = reqparse.RequestParser()
        parse.add_argument('audio', type=FileStorage, location='files')

        args = parse.parse_args()

        audio = args['audio']
        wav_file = wave.open(audio.stream, 'rb')
        signal = wav_file.readframes(-1)
        signal = np.fromstring(signal, 'Int16')
        fs = wav_file.getframerate()
        wav_file.close()
api.add_resource(UploadWaveApi, '/wave')

if __name__ == "__main__":
    app.run(host='0.0.0.0', port = 5555, debug=True)





