package com.hk.framework;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.util.ResourceBundle;
import com.hk.framework.JavaFxApplication.Configuration;
import org.hamcrest.CoreMatchers;
import org.junit.Test;


public class JavafxApplicationTest {

	@Test
	public void testConfigure() {
		MockApplication mockApplication = mock(MockApplication.class);
		JavaFxApplication.Configuration configuration = new JavaFxApplication.Configuration("MockApp", "Root", 100,
				50);

		when(mockApplication.buildGlobalConfiguration()).thenReturn(
				configuration);

		Configuration actual = mockApplication.buildGlobalConfiguration();
		assertThat(actual, is(CoreMatchers.<Object>notNullValue()));
		assertThat(actual.getApplicationTitle(), CoreMatchers.is("MockApp"));
		assertThat(actual.getRootSceneName(), CoreMatchers.is("Root"));
		assertThat(actual.getSceneWidth(), CoreMatchers.is(100.0));
		assertThat(actual.getSceneHeight(), CoreMatchers.is(50.0));
		assertThat(actual.getFxmlExtension(), CoreMatchers.is(".fxml"));
		assertThat(actual.getStylesheetNames(), CoreMatchers.is(new String[]{}));

		verify(mockApplication).buildGlobalConfiguration();
	}

	@Test
	public void testSwitchScene() {
		MockApplication mockApplication = mock(MockApplication.class);
		MockController mockController = mock(MockController.class);

		when(mockApplication.switchScene("Test")).thenReturn(mockController);

		assertThat(mockApplication.switchScene("Test"), CoreMatchers.is(mockController));

		assertThat(mockApplication.switchScene("HOGE"), CoreMatchers.is(CoreMatchers.<Object>nullValue()));

		verify(mockApplication).switchScene("Test");
	}

	public static class MockApplication extends JavaFxApplication {
		@Override
		protected Configuration buildGlobalConfiguration() {
			return null;
		}
	}

	public static class MockController extends JavaFxController {

		@Override
		public void initialize(URL location, ResourceBundle resources) {
		}
	}
}
