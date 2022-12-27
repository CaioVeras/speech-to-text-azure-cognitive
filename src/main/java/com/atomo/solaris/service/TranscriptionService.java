package com.atomo.solaris.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.LoggerFactory;

import com.microsoft.cognitiveservices.speech.CancellationDetails;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import ch.qos.logback.classic.Logger;


public class TranscriptionService {

	static Logger logger = (Logger) LoggerFactory.getLogger(TranscriptionService.class);
   // This example requires environment variables named "SPEECH_KEY" and "SPEECH_REGION"
    private static String speechKey = System.getenv("SPEECH_KEY");
    private static String speechRegion = System.getenv("SPEECH_REGION");
    private static String recordfolder = System.getenv("FOLDER");
    

    public static boolean transcript() throws InterruptedException, ExecutionException {
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechKey, speechRegion);
        speechConfig.setSpeechRecognitionLanguage("en-US");
        
		return recognizeFromMicrophone(speechConfig);
    }
    
    public static boolean recognizeFromMicrophone(SpeechConfig speechConfig) throws InterruptedException, ExecutionException {
        //AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
    	
        logger.warn("speechKey --> " + speechKey);
        logger.warn("speechRegion --> " + speechRegion);
        logger.warn("recordfolder --> " + recordfolder);
        
        
    	logger.warn("Connecting to the folder");
    	AudioConfig audioConfig = AudioConfig.fromWavFileInput(recordfolder);
    	logger.warn("Audio Retrieved -- " + audioConfig.toString());
    	SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, audioConfig);
        
        try {
			
        	logger.warn("Loading the audio track.");
        	Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
        	SpeechRecognitionResult speechRecognitionResult = task.get();
        	
        	if (speechRecognitionResult.getReason() == ResultReason.RecognizedSpeech) {
        		logger.warn("RECOGNIZED: Text=" + speechRecognitionResult.getText());
        	}
        	else if (speechRecognitionResult.getReason() == ResultReason.NoMatch) {
        		logger.warn("NOMATCH: Speech could not be recognized.");
        	}
        	else if (speechRecognitionResult.getReason() == ResultReason.Canceled) {
        		CancellationDetails cancellation = CancellationDetails.fromResult(speechRecognitionResult);
        		logger.warn("CANCELED: Reason=" + cancellation.getReason());
        		
        		if (cancellation.getReason() == CancellationReason.Error) {
        			logger.warn("CANCELED: ErrorCode=" + cancellation.getErrorCode());
        			logger.warn("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
        			logger.warn("CANCELED: Did you set the speech resource key and region values?");
        			return false;
        		}
        	}
        	logger.warn("Closing the service.");
        	return true;
        	
		} catch (Exception e) {
			logger.error("ERROR: Reason=" + e.toString());
			return false;
		}
        //System.exit(0);
    }

}
