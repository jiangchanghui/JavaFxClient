package com.hk.framework.ui;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang.StringUtils;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Created by jiangch on 2014/6/24.
 */
public class SymbolTextField extends CustomTextField {

	private SimpleStringProperty selectedSymbol = new SimpleStringProperty();
	private Label rightLabel = new Label();
	public SymbolTextField() {
		ArrayList<SecurityData> possibleSecurities = new ArrayList<>();
		possibleSecurities.add(new SecurityData("600000","浦发银行"));
		possibleSecurities.add(new SecurityData("000001","深发展"));
		SecuritySuggestionProvider securitySuggestionProvider = new SecuritySuggestionProvider();
		securitySuggestionProvider.addPossibleSuggestions(possibleSecurities);
		new AutoCompletionCustomTextFieldBinding(this,securitySuggestionProvider);
		setRight(rightLabel);
		textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
				if(StringUtils.equals(s2,getSelectedSymbol())==false){
					setSelectedSymbol(null);
					updateRightLabel("");
				}
			}
		});
		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
				if(aBoolean2==false){
					if(StringUtils.equals(getText(),getSelectedSymbol())==false){
						setText("");
						updateRightLabel("");
						setSelectedSymbol(null);
					}
				}
			}
		});

	}

	public String getSelectedSymbol() {
		return selectedSymbol.get();
	}

	public SimpleStringProperty selectedSymbolProperty() {
		return selectedSymbol;
	}

	public void setSelectedSymbol(String selectedSymbol) {
		this.selectedSymbol.set(selectedSymbol);
	}

	public void updateRightLabel(String name){
		this.rightLabel.setText(name);
	}

	class SecurityDataConverter extends StringConverter<SecurityData>{

		@Override
		public String toString(SecurityData securityData) {
			return securityData.getSymbol();
		}

		@Override
		public SecurityData fromString(String s) {
			return null;
		}
	}

	class SecuritySuggestionProvider extends SuggestionProvider<SecurityData> {

		private final Comparator<SecurityData> stringComparator = new Comparator<SecurityData>() {

			@Override
			public int compare(SecurityData o1, SecurityData o2) {
				String o1str = o1.getSymbol();
				String o2str = o2.getSymbol();
				return o1str.compareTo(o2str);
			}
		};

		@Override
		protected Comparator<SecurityData> getComparator() {
			return stringComparator;
		}

		@Override
		protected boolean isMatch(SecurityData suggestion, AutoCompletionBinding.ISuggestionRequest request) {
			String userTextLower = request.getUserText().toLowerCase();
			String suggestionStr = suggestion.getSymbol().toLowerCase();
			return suggestionStr.contains(userTextLower)
					&& !suggestionStr.equals(userTextLower);
		}
	}

	class AutoCompletionCustomTextFieldBinding extends AutoCompletionTextFieldBinding<SecurityData>{

		public AutoCompletionCustomTextFieldBinding(SymbolTextField textField, Callback suggestionProvider) {
			super(textField, suggestionProvider);
		}

		public AutoCompletionCustomTextFieldBinding(SymbolTextField textField, Callback suggestionProvider, StringConverter converter) {
			super(textField, suggestionProvider, converter);
		}

		@Override
		protected void completeUserInput(SecurityData completion) {
			getCompletionTarget().setText(completion.getSymbol());
			getCompletionTarget().updateRightLabel(completion.getName());
			getCompletionTarget().setSelectedSymbol(completion.getSymbol());
			getCompletionTarget().positionCaret(completion.getSymbol().length());
		}

		@Override
		public SymbolTextField getCompletionTarget() {
			return (SymbolTextField) super.getCompletionTarget();
		}

	}
}
