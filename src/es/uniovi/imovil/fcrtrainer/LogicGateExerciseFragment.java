
package es.uniovi.imovil.fcrtrainer;


import android.content.res.TypedArray;
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

public class LogicGateExerciseFragment extends BaseExerciseFragment  implements OnClickListener{
	Button buttoncheck;
	String [] logicstring;
	View rootView;
	TextView logicgate;
	int contador;
	int[] myImageList;
	TypedArray arrayimage;
	ImageView imageview;
	ImageView imageviewsolution;
	EditText edit;
	Button buttonsolution;
	Handler handler;

	public static LogicGateExerciseFragment newInstance() {

		LogicGateExerciseFragment fragment = new LogicGateExerciseFragment();
		return fragment;
	}

	public LogicGateExerciseFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Inicializamos la variable contador con el fin de recorrer el array con las diferentes puertas 
		//logicas
		contador=0;
		
		//Inflamos el Layout
		rootView = inflater.inflate(R.layout.fragment_logic_gate, container, false);
		logicgate = (TextView) rootView.findViewById(R.id.logic_gate);

		//Cargamos el array con las puertas logicas
		logicstring= getResources().getStringArray(R.array.logic_gates);
		
		//Inicializamos las vistas de los botones y sus respectivos Listener
		buttoncheck=(Button) rootView.findViewById(R.id.buttonlogicgate);
		buttonsolution=(Button)rootView.findViewById(R.id.button_logic_gate_solution);
		buttoncheck.setOnClickListener(this);
		buttonsolution.setOnClickListener(this);
		
		//Fijamos la primera puerta lógica
		logicgate.setText(logicstring[0]);
		
		//Cargamos un array con las imagenes de las puertas logicas
		arrayimage = getResources().obtainTypedArray(R.array.logic_gates_images);
		
		//Inicializamos el manejador
		handler= new Handler();
		
		//Inicializamos las vistas de las imagenes
		imageview=(ImageView) rootView.findViewById(R.id.imagelogicgate);
		imageviewsolution=(ImageView) rootView.findViewById(R.id.image_logic_gate_solution);
		

		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.buttonlogicgate:
			//Metodo que comprueba la respuesta
			CompruebaRespuesta();	
			break;
			
		case R.id.button_logic_gate_solution:
			//Mostramos la solución
			solutionLogicGate();
			break;
		}

	}
	

public void CompruebaRespuesta(){
	//Buscamos el id del textID y cogemos el valor que tiene en ese momento y
	//lo ponemos en mayúsculas para compararlo

	edit=(EditText) rootView.findViewById(R.id.edit);
	Editable texto= edit.getText();
	final String textos= texto.toString();
	final String textosUpper=textos.toUpperCase();
	/*Se comprueba si lo que hay en la posicion del string fijada por el contodar es igual a
	lo que hay dentro del edittext*/

	if(logicstring[contador].equals(textosUpper)){
		
		//Si el contador es menor que la longitud del string		
		if(contador<logicstring.length-1){
			//Ponemos el texto en verde y ponemos la imagen de un tic verde.
			edit.setTextColor(Color.GREEN);
			edit.setText(textos);
        	imageviewsolution.setImageResource(R.drawable.correct);      	
        	
        	//Ponemos un delay de 1.5 segundos antes de cambiar de pregunta 
		        handler.postDelayed(new Runnable() {
		            public void run() {
		            	imageviewsolution.setImageResource(0);  
		            	contador++;
		    			edit.setTextColor(Color.BLACK);
		    			logicgate.setText(logicstring[contador]);
		    			edit.setText("");
		    			imageview.setImageResource(arrayimage.getResourceId(contador, 0));
		            }
		        }, 1500);			
		}
		
		// Si no, cuando ya se ha recorrido el string, se pone invisible todo el layout
		//y solo se muestra un texto de que se ha acabado
		
		else {
			imageview.setVisibility(ImageView.GONE);
			logicgate.setVisibility(TextView.VISIBLE);
			buttoncheck.setVisibility(Button.GONE);
			edit.setVisibility(EditText.GONE);
			logicgate.setText(R.string.done_logic_gate);
			arrayimage.recycle();
		}
	}
	
	//Si no es igual es texto del string con el del editText
	
	else {	
		//Se pone el texto en rojo y se carga una imagen de una X roja
			edit.setTextColor(Color.RED);
			edit.setText(textos);
			imageviewsolution.setImageResource(R.drawable.incorrect);
			
			//Ponemos un delay de 1.5 segundos antes de volver a poner el texto en negro y volver
			//a intentarlo
			 handler.postDelayed(new Runnable() {
		            public void run() {
		            	edit.setTextColor(Color.BLACK);
		    			edit.setText(textos);
		    			imageviewsolution.setImageResource(0);
		            }
		        }, 1500);
	}	
}

public void solutionLogicGate(){
	//Ponemos el texto en negro y escribimos en el editText la solución
	edit.setTextColor(Color.BLACK);
	edit.setText(logicstring[contador]);
}
}
