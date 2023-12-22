import pickle
import librosa
import numpy as np
from pydub import AudioSegment
from tensorflow.keras.models import Sequential, model_from_json

import os
from flask_cors import CORS
from flask import Flask, request, jsonify, render_template


# MACHINE LEARNING ========================================================================================

model_json_path = './model/model.json'
model_weights_path = './model/model_weights.h5'
scaler_path = './model/scaler2.pickle'
encoder_path = './model/encoder2.pickle'

json_file = open(model_json_path, 'r')
loaded_model_json = json_file.read()
json_file.close()
loaded_model = model_from_json(loaded_model_json)
loaded_model.load_weights(model_weights_path)

with open(scaler_path, 'rb') as f:
    scaler2 = pickle.load(f)

with open(encoder_path, 'rb') as f:
    encoder2 = pickle.load(f)

def zcr(data, frame_length, hop_length):
    zcr = librosa.feature.zero_crossing_rate(data, frame_length=frame_length, hop_length=hop_length)
    return np.squeeze(zcr)

def rmse(data, frame_length=2048, hop_length=512):
    rmse = librosa.feature.rms(y=data, frame_length=frame_length, hop_length=hop_length)
    return np.squeeze(rmse)

def mfcc(data, sr, frame_length=2048, hop_length=512, flatten: bool = True):
    mfcc = librosa.feature.mfcc(y=data, sr=sr)
    return np.squeeze(mfcc.T) if not flatten else np.ravel(mfcc.T)

def extract_features(data, sr=22050, frame_length=2048, hop_length=512):
    result = np.array([])

    result = np.hstack((result, zcr(data, frame_length, hop_length),
        rmse(data, frame_length, hop_length),
        mfcc(data, sr, frame_length, hop_length)
    ))
    return result

def get_predict_feat(path):
    d, s_rate = librosa.load(path, duration=35.0, offset=0.3)
    res = extract_features(d)
    result = np.array(res)
    target_shape = (1, 33176)
    zeroes_array = np.zeros(target_shape)
    zeroes_array[0, :result.size] = result
    i_result = scaler2.transform(zeroes_array)
    final_result = np.expand_dims(i_result, axis=2)
    return final_result

category_naghams={
        1:'Bayati Ashli Qorror',
        2:'Bayati Ashli Nawa',
        3:'Bayati Syuri',                         
        4:'Bayati Syuri',           
        5:'Bayati Syuri',
        6:'Bayati Husaini',                       
        7:'Bayati Husaini',
        8:'Bayati Husaini',                       
        9:'Bayati Husaini',         
        10:'Bayati Husaini',
        11:'Bayati Husaini',
        12:'Bayati Ashli Jawab',        
        13:'Bayati Ashli Jawab',              
        14:'Bayati Ashli Jawab',
        15:'Bayati Syuri Jawabuljawab',   
        47:'Bayati Syuri Jawabuljawab',
        16:'Shaba Ashli',               
        17:'Shaba Ashli',                     
        18:'Shaba Ashli',
        19:'Shaba Ashli',
        20:'Jawab Shaba',
        21:'Jawab Shaba Maalajam',
        22:'Jawab Shaba Maalbastanjar',
        23:'Hijaz Ashli',               
        24:'Hijaz Ashli',             
        25:'Hijaz Ashli',
        26:'Hijaz Ashli',             
        27:'Hijaz Ashli',             
        28:'Hijaz Ashli',
        29:'Hijaz Kar',
        30:'Hijaz Karkur',
        31:'Hijaz Kur',
        32:'Rast Ashli',                
        33:'Rast Ashli',              
        34:'Rast Ashli',
        35:'Rast Ashli',
        36:'Rast Alanawa',            
        37:'Rast Alanawa',            
        38:'Rast Alanawa',
        48:'Rast zunjiran',
        39:'Sika Ashli',
        40:'Sika Turki',
        41:'Sika Mishri',
        42:'Jiharkah Ashli',
        43:'Jiharkah Jawab',            
        44:'Jiharkah Jawab',
        45:'Nahawand Ashli',
        46:'Nahawand Jawab'
    }

def fix_and_predict(file_path):
    fixed_file_path = fix_wav(file_path)
    res = get_predict_feat(fixed_file_path)
    predictions = loaded_model.predict(res)
    nagham_index = np.argmax(predictions[0])
    nagham_label = category_naghams[nagham_index + 1]
    return nagham_label

def fix_wav(file_path):
    audio = AudioSegment.from_file(file_path)
    audio.export(file_path, format="wav")
    return file_path

# VALIDATION & PROCESS ====================================================================================

ALLOWED_EXTENSIONS = {'wav', 'mp3'}

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

def process_file(uploaded_file):
    try:
        if not allowed_file(uploaded_file.filename):
            return {'error': 'Only .wav files are allowed'}, 400
        file_path = uploaded_file
        predicted_label = fix_and_predict(file_path)
        return {'message': predicted_label}, 200
    except Exception as e:
        return {'error': str(e)}, 500


# FLASK API ===============================================================================================

app = Flask(__name__)
CORS(app)

valid_api_key = "THhUy6ioBEOHKunkb0RIyqfUWiJQSgpA9JiRAsSnclRVmIVkZmkLIK6uuao7FSq2"

def api_key_required(func):
    def wrapper(*args, **kwargs):
        api_key = request.headers.get("API-Key")
        if api_key is None or api_key != valid_api_key:
            return jsonify({"error": "Invalid API key"}), 401
        return func(*args, **kwargs)
    return wrapper

@app.route("/")
def home():
    return render_template("index.html")

@app.route('/predict', methods=['POST'])
@api_key_required
def handle_upload():
    try:
        uploaded_file = request.files['file']
        if uploaded_file:
            result, status_code = process_file(uploaded_file)
            return jsonify(result), status_code
        else:
            return jsonify({'error': 'No file uploaded'}), 400
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
    