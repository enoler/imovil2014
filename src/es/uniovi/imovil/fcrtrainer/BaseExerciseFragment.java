/*

Copyright 2014 Profesores y alumnos de la asignatura Inform�tica M�vil de la EPI de Gij�n

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
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase base de la que deben heredar todos los fragmentos de los ejercicios.
 * Contendr� toda la funcionalidad com�n a todos los ejercicios. Entre ellos
 * est� un bot�n en la barra de tareas para empezar y parar de jugar.
 * 
 * Para que esto funcione, el fragmento debe tener definido un TextView llamado
 * text_view_clock, que debe ser inicialmente invisible. Este TextView servir�
 * para mostrar el reloj mientras se est� jugando. Cuando se pulse el bot�n de
 * jugar, se mostrar� el reloj y empezar� la cuenta atr�s. Cuando se llegue a
 * cero, se acabar� el juego.
 * 
 * Un fragmento con un ejercicio puede hacer estas cosas:
 * 
 * - Redefinir el m�todo playGame() para empezar una partida. Aqu�, t�picamente,
 * se cambiar� el layout para el modo juego y se generar� la primera pregunta.
 * 
 * - Redefinir el m�todo stopGame() para hacer las acciones que se deseen cuando
 * se acabe la partida, como por ejemplo, mostrar un mensaje con la puntuaci�n y
 * llamar a HighScoreManager.addScore().
 * 
 * - Redefinir el m�todo cancelGame(), que se llama cuando el usuario cancela el
 * juego, para hacer las acciones que se deseen.
 * 
 * En estros tres m�todos se debe llamar siempre al m�todo padre en
 * BaseExercise, ya que son los que controlan el comportamiento del juego.
 * 
 * Tambi�n se puede llamar en el constructor del fragmento al m�todo
 * setGameDuration() si se desea una duraci�n distinta de los dos minutos que se
 * tienen por defecto.
 */
public abstract class BaseExerciseFragment extends Fragment {

	private final static int CLOCK_UPDATE_PERIOD_MS = 1000; // 1 s
	private final static int DEFAULT_GAME_DURATION_MS = 120 * 1000; // 2 min

	boolean mIsPlaying = false;

	private Handler mTimerHandler = new Handler();
	private Runnable mUpdateTimeTask = new TimeUpdater();
	private TextView mClock;
	private long mDurationMs = DEFAULT_GAME_DURATION_MS;
	private long mStartMs;

	private final class TimeUpdater implements Runnable {
		public void run() {
			mTimerHandler.removeCallbacks(mUpdateTimeTask);

			long nowMs = System.currentTimeMillis();
			long remainingTimeMs = mDurationMs - (nowMs - mStartMs);
			int remainingTimeSec = (int) (remainingTimeMs / 1000);
			int remainingTimeMin = remainingTimeSec / 60;
			remainingTimeSec = remainingTimeSec % 60;

			final long tenSecondsInMs = 10000;
			if (remainingTimeMs < tenSecondsInMs) {
				mClock.setTextColor(Color.RED);
			} else {
				mClock.setTextColor(Color.BLACK);
			}

			mClock.setText(String.format("%d:%02d", remainingTimeMin,
					remainingTimeSec));

			if (remainingTimeMin > 0 || remainingTimeSec > 0) {
				mTimerHandler.postDelayed(mUpdateTimeTask,
						CLOCK_UPDATE_PERIOD_MS);
			} else {
				endGame();
			}
		}

	}

	public BaseExerciseFragment() {
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.game_control, menu);
		MenuItem item = menu.findItem(R.id.action_change_game_mode);
		try {
			MainActivity activity = (MainActivity) getActivity();
			item.setVisible(!activity.isDrawerOpen());
		} catch (ClassCastException ex) {
			// El fragmento est� incrustado en una actividad distinta a
			// MainActivity. No se hace nada
		}
		if (mIsPlaying) {
			item.setIcon(R.drawable.ic_action_stop);
		} else {
			item.setIcon(R.drawable.ic_action_play);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mIsPlaying) {
			cancelGame();
		}
	}

	private void setClockVisibility(int visibility) {
		if (mClock != null) {
			mClock.setVisibility(visibility);
		}
	}

	void setGameDuration(long durationMs) {
		mDurationMs = durationMs;
	}

	/**
	 * Esta funci�n comienza el juego. En BaseExercise simplemente se hace
	 * visible el reloj y se lanza la tarea que lo actualiza cada segundo. Si se
	 * quiere cambiar la duraci�n del juego, se debe llamar antes a
	 * setGameDuration(). Las clases derivadas deben redifinirla, llamando al
	 * padre, para a�adir lo necesario a cada juego particular
	 */
	void startGame() {
		mClock = (TextView) getView().findViewById(R.id.text_view_clock);
		if (mClock == null) {
			Toast.makeText(getActivity(), getString(R.string.error_no_clock),
					Toast.LENGTH_LONG).show();
			return;
		}

		mIsPlaying = true;
		mStartMs = System.currentTimeMillis();
		setClockVisibility(View.VISIBLE);
		getActivity().supportInvalidateOptionsMenu();

		final long updateTime = 0; // Hacer la primera actualizaci�n
									// inmediatamente
		mTimerHandler.postDelayed(mUpdateTimeTask, updateTime);
	}

	/**
	 * Esta funci�n cancela el juego, parando y ocultando el reloj. Las clases
	 * derivadas deben redifinirla, llamando al padre, para a�adir lo necesario
	 * a cada juego particular
	 */
	void cancelGame() {
		stopPlaying();
	}

	/**
	 * Esta funci�n se llama al finalizar el juego, parando y ocultando el
	 * reloj. Las clases derivadas deben redifinirla, llamando al padre, para
	 * a�adir lo necesario a cada juego particular
	 */
	void endGame() {
		stopPlaying();
	}

	private void stopPlaying() {
		if (mIsPlaying) {
			mIsPlaying = false;
			mTimerHandler.removeCallbacks(mUpdateTimeTask);
			setClockVisibility(View.GONE);
			getActivity().supportInvalidateOptionsMenu();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_change_game_mode:
			if (mIsPlaying) {
				cancelGame();
			} else {
				startGame();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
