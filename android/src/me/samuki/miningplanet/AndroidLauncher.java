package me.samuki.miningplanet;

import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import me.samuki.miningplanet.GameClass;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GameClass(), config);
	}

	private void hideSystemUI() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	@Override
	public void onResume() {
		super.onResume();
		hideSystemUI();
		//if (adView != null) adView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		hideSystemUI();
		//if (adView != null) adView.pause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hideSystemUI();
		//if (adView != null) adView.destroy();
	}


}
