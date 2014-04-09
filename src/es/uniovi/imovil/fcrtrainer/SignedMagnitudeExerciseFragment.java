/*

Copyright 2014 Profesores y alumnos de la asignatura Inform?tica M?vil de la EPI de Gij?n

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package es.uniovi.imovil.fcrtrainer;

import android.graphics.Color; 
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SignedMagnitudeExerciseFragment extends BaseExerciseFragment implements OnClickListener {
	
	private String SMquestions[];
	private String SManswers[];
	private TextView textViewQuestion;
	private ImageView imageView;
	private Button checkAnswer;
	private Button giveAnswer;
	private EditText editTextAnswer;
	private Editable text;
	private String answer;
	private Handler imageHandler;
	private int nQuestion=0;
	
	
	public static SignedMagnitudeExerciseFragment newInstance() {
		
		SignedMagnitudeExerciseFragment fragment = new SignedMagnitudeExerciseFragment();
		return fragment;
	}
	
	public SignedMagnitudeExerciseFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView;
		rootView = inflater.inflate(R.layout.fragment_signed_magnitude, container, false);
		
		//Cargamos los valores de arrays.xml
		SMquestions=getResources().getStringArray(R.array.signed_magnitude_questions);
		SManswers=getResources().getStringArray(R.array.signed_magnitude_answers);
		
		
		textViewQuestion = (TextView)rootView.findViewById(R.id.textViewPregunta);
		
		//Relleno el TextView con la primera pregunta
		textViewQuestion.setText(SMquestions[nQuestion]);
		
		checkAnswer = (Button)rootView.findViewById(R.id.check_answer);
		giveAnswer = (Button)rootView.findViewById(R.id.give_solution);
		imageView = (ImageView)rootView.findViewById(R.id.imageViewCheck);
		checkAnswer.setOnClickListener(this);
		giveAnswer.setOnClickListener(this);
		imageHandler = new Handler();
		
		editTextAnswer = (EditText)rootView.findViewById(R.id.editTextRespuesta);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.check_answer:
			text = editTextAnswer.getText();
			answer = text.toString();
			//Si la respuesta es correcta...
			if(answer.equals(SManswers[nQuestion])){
				imageView.setImageResource(R.drawable.correct);
				editTextAnswer.setTextColor(Color.GREEN);
				editTextAnswer.setText(answer);
				if(nQuestion==SMquestions.length-1){
					nQuestion=0;
				}
				else {
					nQuestion++;
				}
				/*  1,5 segundos después hago que la imagen desaparezca, paso a la siguiente
				 *  pregunta, vacío el editText y volvemos a poner su color en negro */
				imageHandler.postDelayed(new Runnable() {
					public void run() {
						imageView.setImageResource(0);
						textViewQuestion.setText(SMquestions[nQuestion]);
						editTextAnswer.setText("");
						editTextAnswer.setTextColor(Color.BLACK);
					}
				}, 1500);
			}
			//Si la respuesta es incorrecta...
			else{
				imageView.setImageResource(R.drawable.incorrect);
				editTextAnswer.setTextColor(Color.RED);
				editTextAnswer.setText(answer);
				
				/*  1,5 segundos después hago que la imagen desaparezca, vacío el editText
				 *  y volvemos a poner su color en negro */
				imageHandler.postDelayed(new Runnable() {
					public void run() {
						imageView.setImageResource(0);
						editTextAnswer.setText("");
						editTextAnswer.setTextColor(Color.BLACK);
					}
				}, 1500);
			}
			
			break;
			
		
		case R.id.give_solution:
			editTextAnswer.setTextColor(Color.RED);
			editTextAnswer.setText(SManswers[nQuestion]);
			if(nQuestion==SMquestions.length-1){
				nQuestion=0;
			}
			else {
				nQuestion++;
			}
			/*  2 segundos después actualizo a la siguiente pregunta, hago que la 
			 *  imagen desaparezca, vacío el editText y volvemos a poner su color
			 *  en negro */
			imageHandler.postDelayed(new Runnable() {
				public void run() {
					textViewQuestion.setText(SMquestions[nQuestion]);
					imageView.setImageResource(0);
					editTextAnswer.setText("");
					editTextAnswer.setTextColor(Color.BLACK);
				}
			}, 2000);
			
			break;
			
		}
		
	}
	
}
