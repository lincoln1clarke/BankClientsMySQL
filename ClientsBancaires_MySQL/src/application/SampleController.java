package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SampleController implements Initializable{

    @FXML
    private TextField networthBox;

    @FXML
    private TextField nameBox;

    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TextField ageBox;

    @FXML
    private TableColumn<Client, String> riskColumn;

    @FXML
    private Button addBtn;

    @FXML
    private Button eraseBtn;

    @FXML
    private TableColumn<Client, String> nameColumn;

    @FXML
    private TableColumn<Client, Integer> ageColumn;

    @FXML
    private Button modifyBtn;

    @FXML
    private TableColumn<Client, Long> networthColumn;

    @FXML
    private ComboBox<String> riskLevelCBO;

    @FXML
    private TableColumn<Client, Long> moneyColumn;

    @FXML
    private TextField lastNameBox;

    @FXML
    private TableColumn<Client, String> lastNameColumn;

    @FXML
    private TextField moneyBox;

    @FXML
    private Button restartBtn;
    
    private ObservableList<String> riskLevels = FXCollections.observableArrayList("Low", "Medium", "High");
    
    private ObservableList<Client> clientData = FXCollections.observableArrayList();
    
    /*public ObservableList<Client> getClientData(){
    	return clientData;
    }*/
    
    @FXML
    void add(ActionEvent event) {
    	
    	if(inputValid()) {
    		Client tmp = new Client();
        	tmp.setName(nameBox.getText());
        	tmp.setLastName(lastNameBox.getText());
        	tmp.setAge(Integer.parseInt(ageBox.getText()));;
        	tmp.setMoney(Long.parseLong(moneyBox.getText()));;
        	tmp.setNetworth(Long.parseLong(networthBox.getText()));
        	tmp.setRiskLevel(riskLevelCBO.getValue());
        	clientData.add(tmp);
        	
        	modifyBtn.setDisable(false);
        	eraseBtn.setDisable(false);
        	restartBtn.setDisable(false);
        	
        	appendOnSQL(establishedConnection(), tmp);
        	
        	clearFields();
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("ERROR");
    		alert.setTitle("Caution");
    		alert.setContentText("Please complete all fields with appropriate input before proceding.\nNumber values only accept whole numbers.");
    		alert.show();
    	}
    }
    
    public boolean inputValid() {
    	boolean valid = true;
    	
    	//System.out.println(riskLevelCBO.getSelectionModel().getSelectedIndex());
    	
    	try {
			int number = Integer.parseInt(ageBox.getText());
		}catch(Exception e){
			valid = false;
			ageBox.setText(null); 
		}
			
    	try {
    		long number1 = Long.parseLong(moneyBox.getText());
    	}catch(Exception e1){
    		valid = false;
    		moneyBox.setText(null);
    		}
    	
    	try {
    		long number2 = Long.parseLong(networthBox.getText());
    	}catch(Exception e2){
    		valid = false;
    		networthBox.setText(null);
    		}
    	
    	if(riskLevelCBO.getSelectionModel().getSelectedIndex() == -1) {
    		valid = false;
    		}
    	return valid;
    	}
    
    void clearFields() {
    	nameBox.setText(null);
    	lastNameBox.setText(null);
    	ageBox.setText(null);
    	moneyBox.setText(null);
    	networthBox.setText(null);
    	riskLevelCBO.setValue(null);
    }
    @FXML
    void clearFieldsPressed(ActionEvent event) {
    	clearFields();
    }
    void showClient(Client client) {
    	if (client != null){
    		nameBox.setText(client.getName());
    		lastNameBox.setText(client.getLastName());
    		ageBox.setText(Integer.toString(client.getAge()));
    		moneyBox.setText(Long.toString(client.getMoney()));
    		networthBox.setText(Long.toString(client.getNetworth()));
    		riskLevelCBO.setValue(client.getRiskLevel());
    	}else {
    		clearFields();
    	}
    }

    @FXML
    void modify(ActionEvent event) {
    	Client tmp = clientsTable.getSelectionModel().getSelectedItem();
    	
    	tmp.setName(nameBox.getText());
    	tmp.setLastName(lastNameBox.getText());
    	tmp.setAge(Integer.parseInt(ageBox.getText()));;
    	tmp.setMoney(Long.parseLong(moneyBox.getText()));;
    	tmp.setNetworth(Long.parseLong(networthBox.getText()));
    	tmp.setRiskLevel(riskLevelCBO.getValue());
    	clientsTable.refresh();
    }

    @FXML
    void erase(ActionEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Restart?");
    	alert.setHeaderText("Caution");
    	alert.setContentText("Are you sure you want to erase this client?");
    	Optional<ButtonType> result= alert.showAndWait();
    	if(result.get() == ButtonType.OK) {
    		int index = clientsTable.getSelectionModel().getSelectedIndex();
        	if(index>=0) {
        		clientsTable.getItems().remove(index);
        		//remove client with id of index + 1 from database
        	}
    	}
    }

    @FXML
    void restart(ActionEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Restart?");
    	alert.setHeaderText("Caution");
    	alert.setContentText("Are you sure you want to delete all clients from the database?");
    	Optional<ButtonType> result= alert.showAndWait();
    	if(result.get() == ButtonType.OK) {
    		clearFields();
        	while(clientsTable.getItems().size()>0) {
        		clientsTable.getItems().remove(0);
        	}
    	}
    }
    
    public Connection establishedConnection() {
    	String myDriver = "com.mysql.cj.jdbc.Driver";
    	String myURL = "jdbc:mysql://lessonplus.com:3306/lessonpl_lincoln?"
    			+"zeroDateTimeBehaviour=CONVERT_TO_NULL&serverTimezone=UTC";
    	String myUsername = "lessonpl_lincoln";
    	String myPassword = "LQEX$rvKUKhP";
    	try {
    		Class.forName(myDriver);
    		Connection connection = DriverManager.getConnection(myURL, myUsername, myPassword);
    		return connection;
    	}catch(Exception e) {
    		Connection connection = null;
    		return connection;
    	}
    }
    
    public void appendOnSQL(Connection connection, Client client) {
    	if(connection != null) {
    		try {
    			Statement statement = connection.createStatement();
    			statement.executeUpdate("Insert into Bank_Clients (firstName, lastName, age, moneyInvested, networth, riskLevel)" + "VALUES ('" + client.getName() + "','" + client.getLastName() + "','" + Integer.toString(client.getAge()) + "','" + Long.toString(client.getMoney()) + "','" + Long.toString(client.getNetworth()) + "','" + client.getRiskLevel() +"')");
    			connection.close();
    		}catch (Exception e) {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setHeaderText("ERROR");
    			alert.setTitle("Caution");
    			alert.setContentText("Unable to save data");
    			alert.show();
    		}
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("ERROR");
    		alert.setTitle("Caution");
    		alert.setContentText("Unable to establish a connection with the server");
    		alert.show();
    	}
    	//System.out.println("Insert into Bank_Clients (firstName, lastName, age, moneyInvested, networth, riskLevel)" + "VALUES ('" + client.getName() + "','" + client.getLastName() + "','" + Integer.toString(client.getAge()) + "','" + Long.toString(client.getMoney()) + "','" + Long.toString(client.getNetworth()) + "','" + client.getRiskLevel() +"')");
    }
    
    public void loadDataFromSQL(Connection connection) {
    	if(connection != null) {
    		try {
    			PreparedStatement statement = connection.prepareStatement("select * from Bank_Clients");
    			ResultSet result = statement.executeQuery();
    			while(result.next()) {
    				Client tmp = new Client(result.getString("firstName"), result.getString("lastName"));
    				tmp.setAge(Integer.parseInt(result.getString("age")));    
    				tmp.setMoney(Long.parseLong(result.getString("moneyInvested")));
    				tmp.setNetworth(Long.parseLong(result.getString("networth")));
    				tmp.setRiskLevel(result.getString("riskLevel"));
    				clientData.add(tmp);
    			}
    			
    			connection.close();
    		}catch (Exception e) {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setHeaderText("ERROR");
    			alert.setTitle("Caution");
    			alert.setContentText("Unable to load data");
    			alert.show();
    		}
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("ERROR");
    		alert.setTitle("Caution");
    		alert.setContentText("Unable to establish a connection with the server");
    		alert.show();
    	}
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		riskLevelCBO.setItems(riskLevels);
		//riskLevelCBO.getSelectionModel().selectFirst();
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
		moneyColumn.setCellValueFactory(new PropertyValueFactory<>("money"));
		networthColumn.setCellValueFactory(new PropertyValueFactory<>("networth"));
		riskColumn.setCellValueFactory(new PropertyValueFactory<>("riskLevel"));
		
		modifyBtn.setDisable(true);
		eraseBtn.setDisable(true);
		restartBtn.setDisable(true);
		
		loadDataFromSQL(establishedConnection());
		
		clientsTable.setItems(clientData);
		
		showClient(null);
		
		clientsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->showClient(newValue));
	}

}
