import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class reto4Controller {

    @FXML
    private AnchorPane parent;

    @FXML
    private ImageView misionimg;

    @FXML
    private ImageView uninorteimg;

    @FXML
    private Button modebtn;

    @FXML
    private ImageView modeimg;

    @FXML
    private Label modetxt;

    @FXML
    private TextField nametxt;

    @FXML
    private TextField idtxt;

    @FXML
    private TextField towntxt;

    @FXML
    private TextField ircatxt;

    @FXML
    private TextArea addareatxt;

    @FXML
    private TextArea outputtxt;

    // }
    @FXML
    private Label statustxt;

    @FXML
    private ComboBox<String> sweetcbx;

    @FXML
    private ComboBox<String> typewatercbx;

    @FXML
    private TextField nameEDtxt;

    @FXML
    private TextField idEDtxt;

    @FXML
    private TextField townEDtxt;

    @FXML
    private TextField ircaEDtxt;

    @FXML
    private ComboBox<String> sweetEDcbx;

    @FXML
    private ComboBox<String> typewaterEDcbx;

    @FXML
    private TextField idsearchtxt;

    @FXML
    private Button editbtn;

    @FXML
    private Button deletebtn;

    @FXML
    public void insertClick(MouseEvent event) {
        String name = nametxt.getText().strip();
        String town = towntxt.getText().strip();
        String typewater = typewatercbx.getValue();
        String sweet = sweetcbx.getValue();
        String irca = ircatxt.getText().strip();;
        String id = idtxt.getText().strip();

        int next = 0;

        try {
            next = Integer.parseInt(id) + 1;
        } catch (Exception e) {
            statustxt.setText("Id debe ser un numero entero");
            id = null;
            idtxt.setText("");
        }

        try {
            double ircaTest = Double.parseDouble(irca);
        } catch (Exception e) {
            statustxt.setText("IRCA debe ser un numero real");
            irca = null;
            ircatxt.setText("");
        }
        
        Conexion con = new Conexion();
        con.connect();
        if(con.isConnected()){
            if (name.isEmpty() || id.isEmpty() || town.isEmpty() || irca.isEmpty() || id == null || irca == null) {
                    statustxt.setText("Por favor, llene todos los campos del formulario");
            } else {
                String query = "INSERT INTO Cuerpos (ID, Name, Town, TypeWater, Sweet, IRCA) VALUES ('" + id + "','"+ name + "','"+ town + "','"+ typewater + "','"+ sweet + "','"+ irca + "');";
                try (Statement stm = con.getCon().createStatement()) {
                    int rst = stm.executeUpdate(query);
                    if (rst != 0) {
                        statustxt.setText("Registro agregado exitosamente a la base de datos");
                        nametxt.setText("");
                        idtxt.setText(String.valueOf(next));
                        towntxt.setText("");
                        ircatxt.setText("");
                        addareatxt.setText("");
                        outputtxt.setText("");
                    } else {
                        statustxt.setText("No se ha completado la inserción");
                    }
                } catch (Exception e) {
                    statustxt.setText("El id ya se encuentra en la Base de Datos");
                }
            }
        }
        con.disconnect();
    }

    @FXML
    void getClick(MouseEvent event) {
        addareatxt.setText("");
        outputtxt.setText("");
                
        Conexion con = new Conexion();
        con.connect();

        if(con.isConnected()){
            String query = "SELECT * FROM Cuerpos;";
            try (Statement stm = con.getCon().createStatement()) {
                ResultSet rst = stm.executeQuery(query);
                if (rst.next()) {
                    statustxt.setText("Registros encontrados exitosamente");
                    do {
                        addareatxt.appendText(String.valueOf(rst.getInt("ID")) + "-");
                        addareatxt.appendText(rst.getString("Name") + "-");
                        addareatxt.appendText(rst.getString("TypeWater") + "-");
                        addareatxt.appendText(rst.getString("Town") + "-");
                        addareatxt.appendText(rst.getString("Sweet") + "-");
                        addareatxt.appendText(String.valueOf(rst.getDouble("IRCA")));
                        addareatxt.appendText("\n");
                    } while(rst.next());
                } else {
                    statustxt.setText("Base de datos vacia");
                }
            } catch (Exception e) {
                System.out.println("Base de datos vacia");
            }
        }
        con.disconnect();
    }

    @FXML
    public void runClick(MouseEvent event) {
        outputtxt.setText("");
        ArrayList<CuerpoDeAgua> bodiesOfWater = new ArrayList<CuerpoDeAgua>();
        float amountHighAndLow = 0;
        int amountMed = 0;
        double sum = 0;
                
        Conexion con = new Conexion();
        con.connect();

        if(con.isConnected()){
            String query = "SELECT * FROM Cuerpos;";
            try (Statement stm = con.getCon().createStatement()) {
                ResultSet rst = stm.executeQuery(query);
                if (rst.next()) {
                    statustxt.setText("Registros encontrados exitosamente");
                    do {                        
                        int id = rst.getInt("ID");
                        String name = rst.getString("Name");
                        String typewater = rst.getString("TypeWater");
                        String town = rst.getString("Town");
                        String sweet = rst.getString("Sweet");
                        Double irca = rst.getDouble("IRCA");
                        
                        CuerpoDeAgua tempBody = new CuerpoDeAgua(name, id, town, typewater, sweet, irca);
                        bodiesOfWater.add(tempBody);
                    } while(rst.next());
                } else {
                    statustxt.setText("Base de datos vacia");
                }
            } catch (Exception e) {
                System.out.println("Base de datos vacia");
            }
        }
        con.disconnect();

        for (int i = 0; i < bodiesOfWater.size(); i++) {
            String catTemp = bodiesOfWater.get(i).nivel(bodiesOfWater.get(i).getIrca());
            if(catTemp == "ALTO" || catTemp == "MEDIO")
                amountHighAndLow++;
            sum += bodiesOfWater.get(i).getIrca();
        }
        
        for (int i = 0; i < bodiesOfWater.size(); i++)
            outputtxt.setText(outputtxt.getText() + bodiesOfWater.get(i).getName() + "\n");

        outputtxt.setText(outputtxt.getText() + String.format("%.2f", amountHighAndLow) + "\n");

        for (int i = 0; i < bodiesOfWater.size(); i++){
            String catTemp = bodiesOfWater.get(i).nivel(bodiesOfWater.get(i).getIrca());
            if(catTemp == "ALTO"){
                outputtxt.setText(outputtxt.getText() + bodiesOfWater.get(i).getName() + " ");
                amountMed++;
            }
        }
    
        if (amountMed == 0)
            outputtxt.setText(outputtxt.getText() + "NA");

        outputtxt.setText(outputtxt.getText() + "\n");
        outputtxt.setText(outputtxt.getText() + String.format("%.2f", sum / bodiesOfWater.size()));
        statustxt.setText("Datos procesados con exito");
    }

    @FXML
    void searchClick(MouseEvent event) {
        String id = idsearchtxt.getText().strip();
        
        Conexion con = new Conexion();
        con.connect();

        if(con.isConnected()){
            String query = "SELECT * FROM Cuerpos WHERE ID='" + id + "';";
            try (Statement stm = con.getCon().createStatement()) {
                ResultSet rst = stm.executeQuery(query);
                if (rst.next()) {
                    statustxt.setText("Registro encontrado exitosamente");
                    idEDtxt.setText(String.valueOf(rst.getInt("ID")));
                    nameEDtxt.setText(rst.getString("Name"));
                    townEDtxt.setText(rst.getString("Town"));
                    ircaEDtxt.setText(String.valueOf(rst.getDouble("IRCA")));
                    typewaterEDcbx.setValue(rst.getString("TypeWater"));
                    sweetEDcbx.setValue(rst.getString("Sweet"));
                    editbtn.setDisable(false);
                    deletebtn.setDisable(false);
                } else {
                    statustxt.setText("Id no encontrado");
                    editbtn.setDisable(true);
                    deletebtn.setDisable(true);
                }
            } catch (Exception e) {
                statustxt.setText("Error al buscar en la Base de Datos");
            }
        }
        con.disconnect();
    }

    @FXML
    void editClick(MouseEvent event) {
        String name = nameEDtxt.getText().strip();
        String town = townEDtxt.getText().strip();
        String typewater = typewaterEDcbx.getValue();
        String sweet = sweetEDcbx.getValue();
        String irca = ircaEDtxt.getText().strip();;
        String idNew = idEDtxt.getText().strip();
        String idOld = idsearchtxt.getText().strip();

        try {
            int next = Integer.parseInt(idNew) + 1;
        } catch (Exception e) {
            statustxt.setText("Id debe ser un numero entero");
            idNew = null;
            idtxt.setText("");
        }

        try {
            double ircaTest = Double.parseDouble(irca);
        } catch (Exception e) {
            statustxt.setText("IRCA debe ser un numero real");
            irca = null;
            ircatxt.setText("");
        }
        
        Conexion con = new Conexion();
        con.connect();
        if(con.isConnected()){
            if (name.isEmpty() || idNew.isEmpty() || town.isEmpty() || irca.isEmpty() || idNew == null || irca == null) {
                    statustxt.setText("Por favor, llene todos los campos del formulario");
            } else {
                String query = "UPDATE Cuerpos SET ID='"+ idNew + "', Name='"+ name +"', Town='"+ town +"', TypeWater='"+ typewater +"', Sweet='"+ sweet +"', IRCA = '"+ irca + "' WHERE ID='" + idOld + "';";
                try (Statement stm = con.getCon().createStatement()) {
                    int rst = stm.executeUpdate(query);
                    if (rst != 0) {
                        statustxt.setText("Registro actualizado exitosamente en la base de datos");
                        nameEDtxt.setText("");
                        idEDtxt.setText("");
                        townEDtxt.setText("");
                        ircaEDtxt.setText("");
                        
                        editbtn.setDisable(true);
                        deletebtn.setDisable(true);

                        addareatxt.setText("");
                        outputtxt.setText("");
                    } else {
                        statustxt.setText("No se encontró el registro");
                    }
                } catch (Exception e) {
                    statustxt.setText("Ha habido un error");
                }
            }
        }
        con.disconnect();
    }

    @FXML
    void deleteClick(MouseEvent event) {
        String idOld = idsearchtxt.getText().strip();

        Conexion con = new Conexion();
        con.connect();
        if(con.isConnected()){
            String query = "DELETE FROM Cuerpos WHERE ID='" + idOld + "';";
            try (Statement stm = con.getCon().createStatement()) {
                int rst = stm.executeUpdate(query);
                if (rst != 0) {
                    statustxt.setText("Registro borrado exitosamente en la base de datos");
                    nameEDtxt.setText("");
                    idEDtxt.setText("");
                    townEDtxt.setText("");
                    ircaEDtxt.setText("");
                    
                    editbtn.setDisable(true);
                    deletebtn.setDisable(true);

                    addareatxt.setText("");
                    outputtxt.setText("");
                } else {
                    statustxt.setText("No se encontró el registro");
                }
            } catch (Exception e) {
                statustxt.setText("Ha habido un error");
            }
        }
        con.disconnect();
    }

    @FXML
    void donateClick(MouseEvent event) {
        Alert alert =  new Alert(AlertType.INFORMATION);
        alert.setContentText("click acá: https://www.google.com");
        alert.setTitle("Gracias por tu contribución");
        Hyperlink link = new Hyperlink("Click here!");
        alert.setGraphic(link);
        link.setOnAction(e -> {
            statustxt.setText("Gracias!!!");
        });
        alert.showAndWait();
    }

    private boolean isLightMode = true;

    public void changeMode(MouseEvent event) {
        isLightMode = !isLightMode;
        if (isLightMode) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }

    private void setLightMode() {
        parent.getStylesheets().remove("styles/darkMode.css");
        parent.getStylesheets().add("styles/lightMode.css");
        Image modeImg = new Image("img/moon.png");
        modetxt.setText("Light Mode");
        modeimg.setImage(modeImg);
        Image misionImg = new Image("img/misionLight.png");
        misionimg.setImage(misionImg);
        Image uniImg = new Image("img/uninorteLight.png");
        uninorteimg.setImage(uniImg);
    }

    private void setDarkMode() {
        parent.getStylesheets().remove("styles/lightMode.css");
        parent.getStylesheets().add("styles/darkMode.css");
        Image modeImg = new Image("img/sun-icon.png");
        modetxt.setText("Dark Mode");
        modeimg.setImage(modeImg);
        Image misionImg = new Image("img/misionDark.png");
        misionimg.setImage(misionImg);
        Image uniImg = new Image("img/uninorteDark.png");
        uninorteimg.setImage(uniImg);
    }

    @FXML
    void initialize() {
        sweetcbx.getItems().removeAll(sweetcbx.getItems());
        sweetcbx.getItems().addAll("Dulce", "Salada");
        sweetcbx.getSelectionModel().select("Dulce");

        idtxt.setText("0");

        typewatercbx.getItems().removeAll(typewatercbx.getItems());
        typewatercbx.getItems().addAll("Arroyo", "Caño", "Cienaga", "Lago", "Laguna", "Rio", "Oceano");
        typewatercbx.getSelectionModel().select("Arroyo");

        sweetEDcbx.getItems().removeAll(sweetEDcbx.getItems());
        sweetEDcbx.getItems().addAll("Dulce", "Salada");
        sweetEDcbx.getSelectionModel().select("Dulce");

        typewaterEDcbx.getItems().removeAll(typewaterEDcbx.getItems());
        typewaterEDcbx.getItems().addAll("Arroyo", "Caño", "Cienaga", "Lago", "Laguna", "Rio", "Oceano");
        typewaterEDcbx.getSelectionModel().select("Arroyo");

        editbtn.setDisable(true);
        deletebtn.setDisable(true);
    }
}
