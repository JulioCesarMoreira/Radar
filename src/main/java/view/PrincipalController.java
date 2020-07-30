package view;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;
import model.Veiculo;
import org.apache.commons.lang.RandomStringUtils;

public class PrincipalController implements Initializable {

    @FXML
    private TableView tbVwEntrada;
    @FXML
    private TableView tbVwMulta;
    @FXML
    private ComboBox cbDisponivel;
    @FXML
    private TextField txtFldLimiteV;
    @FXML
    private TextField txtFldDistancia;
    @FXML
    private Label lbUltVerifc;
    @FXML
    private Label lbMaisLento;
    @FXML
    private Label lbMaisRapido;
    @FXML
    private Label lbTempMedio;

    private List<String> lstDisponivel = new ArrayList<>();
    private List<Veiculo> lstEntrada = new ArrayList<>();
    private List<Veiculo> lstMulta = new ArrayList<>();

    double tempoTotal = 0, maiorVeloc = 0, menorVeloc = 2000000;
    int quantidadeSaida = 0;

    private void disponivelVeiculo() {
        Random rand = new Random();
        int alternativa;
        for (int i = lstDisponivel.size(); i <= 4; i++) {
            alternativa = rand.nextInt(10);
            if (alternativa % 2 == 0) {
                lstDisponivel.add(geraPlacaAntiga());
            } else {
                lstDisponivel.add(geraPlacaNova());
            }
        }
        cbDisponivel.setItems(FXCollections.observableList(lstDisponivel));
    }

    private String geraPlacaAntiga() {
        Random rand = new Random();
        String letras = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        StringBuffer numeros = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            int numero = rand.nextInt(9);
            numeros.append(String.valueOf(numero));
        }
        return letras + "-" + numeros.toString();
    }

    // 3 letras 1 numero 1 letra 2 numeros
    private String geraPlacaNova() {
        Random rand = new Random();
        String letrasP = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        String numeroSolo = String.valueOf(rand.nextInt(9)).toUpperCase();
        String letraS = RandomStringUtils.randomAlphabetic(1).toUpperCase();
        StringBuffer numeros = new StringBuffer();
        for (int i = 0; i < 2; i++) {
            int numero = rand.nextInt(9);
            numeros.append(String.valueOf(numero));
        }
        return letrasP + numeroSolo + letraS + numeros.toString();
    }

    private Veiculo VeiculoSel;

    @FXML
    private void btnDisponivelClick() {
        LocalDateTime hora = LocalDateTime.now();
        DateTimeFormatter formatterHR = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaFormatado = hora.format(formatterHR);
        lstEntrada.add(new Veiculo(cbDisponivel.getSelectionModel().getSelectedItem().toString(), horaFormatado,
                Double.valueOf(txtFldDistancia.getText())));
        cbDisponivel.getItems().removeAll(cbDisponivel.getSelectionModel().getSelectedItem());
        disponivelVeiculo();
        tbVwEntrada.refresh();
        cbDisponivel.getSelectionModel().selectFirst();
    }

    @FXML
    private void tblVwSaidaClick(Event event) {
        int index = lstEntrada.indexOf(tbVwEntrada.getSelectionModel().getSelectedItem());
        LocalDateTime hora = LocalDateTime.now();
        DateTimeFormatter formatterHR = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaFormatado = hora.format(formatterHR);
        MouseEvent me = null;
        if (event.getEventType() == MOUSE_CLICKED) {
            me = (MouseEvent) event;
            if (me.getClickCount() == 2) {
                if (index >= 0) {
                    VeiculoSel = lstEntrada.get(index);
                    VeiculoSel.setHoraSaida(horaFormatado);
                    lstEntrada.remove(index);
                    tbVwEntrada.refresh();
                    tempoTotal = tempoTotal + VeiculoSel.getTempo();
                    quantidadeSaida = quantidadeSaida + 1;
                    lbUltVerifc.setText("Placa: " + VeiculoSel.getPlaca() + ". Tempo: " + String.valueOf(VeiculoSel.getTempo()) + " segundos. Velocidade: " + VeiculoSel.getVelocidadeF() + " metros/segundo.");

                    if (VeiculoSel.getVelocidade() > maiorVeloc) {
                        maiorVeloc = VeiculoSel.getVelocidade();
                        lbMaisRapido.setText("Placa: " + VeiculoSel.getPlaca() + ". Tempo: " + String.valueOf(VeiculoSel.getTempo()) + " segundos. Velocidade: " + VeiculoSel.getVelocidadeF() + " metros/segundo.");
                    }
                    if (VeiculoSel.getVelocidade() < menorVeloc) {
                        menorVeloc = VeiculoSel.getVelocidade();
                        lbMaisLento.setText("Placa: " + VeiculoSel.getPlaca() + ". Tempo: " + String.valueOf(VeiculoSel.getTempo()) + " segundos. Velocidade: " + VeiculoSel.getVelocidadeF() + " metros/segundo.");
                    }

                    lbTempMedio.setText(String.format("%.2f", tempoTotal / Double.valueOf(quantidadeSaida)));

                    if (VeiculoSel.getVelocidade() > Double.valueOf(txtFldLimiteV.getText()) && VeiculoSel.getVelocidade() < 240) {
                        lstMulta.add(VeiculoSel);
                        tbVwMulta.refresh();
                    } else if (VeiculoSel.getVelocidade() > 240) {
                        alertaVelociade();
                    }
                }
            }
        }
    }
    
    private void adicionaListener(TextField texto) {
        texto.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d*(\\"
                            + "."
                            + "\\d*)?")
                    && !newValue.isEmpty()) {
                        texto.setText(oldValue);
                    } else {
                        texto.setText(newValue);
                    }
                    habilitaCalcular();
                });
    }

    private void alertaVelociade() {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "O veículo de placa: " + VeiculoSel.getPlaca() + ", está com velociade superior aos 240 KM/hr",
                ButtonType.OK);
        alert.setTitle("Velocidade suspeita!");
        alert.showAndWait();
    }
    
    @FXML
    private void zerarTudoClick(){
        tempoTotal = 0; 
        maiorVeloc = 0; 
        menorVeloc = 2000000;
        quantidadeSaida = 0;
        lbMaisLento.setText("");
        lbMaisRapido.setText("");
        lbTempMedio.setText("");
        lbUltVerifc.setText("");
        tbVwEntrada.getItems().removeAll(tbVwEntrada.getItems());
        tbVwMulta.getItems().removeAll(tbVwMulta.getItems());
        txtFldDistancia.setText("100");
        txtFldLimiteV.setText("11");
        cbDisponivel.getItems().removeAll(cbDisponivel.getItems());
        disponivelVeiculo();
        cbDisponivel.getSelectionModel().selectFirst();
        tbVwEntrada.refresh();
        tbVwMulta.refresh();
    }

    private void habilitaCalcular() {
        tbVwEntrada.setDisable(txtFldDistancia.getText().isEmpty()
                || txtFldLimiteV.getText().isEmpty());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        adicionaListener(txtFldDistancia);
        adicionaListener(txtFldLimiteV);
        tbVwEntrada.setItems(FXCollections.observableList(lstEntrada));
        tbVwMulta.setItems(FXCollections.observableList(lstMulta));
        disponivelVeiculo();
        cbDisponivel.getSelectionModel().selectFirst();
    }

}
// Falta adc listener no txtfld e um alerta pra Veloc a cima de 250km/hr, alem de guardar as innformações que o porblema pede

//    private void disparoUnico(){
//                    t = RandomTemp();
//                    u = RandomUmid();
//                    l = RandomLumi();
//                    adicionaTableView(t, u, l);
//                    MaiorTemp = guardaMaiorTemperatura(t);
//                    MaiorUmid = guardaMaiorUmidade(u);
//                    MaiorLumi = guardaMaiorLuminosidade(l);
//                    gaugeTemp.setValue(t);
//                    gaugeUmid.setValue(u);
//                    gaugeLumi.setValue(l);
//                    gaugeTemp.setThreshold(MaiorTemp);
//                    gaugeUmid.setThreshold(MaiorUmid);
//                    gaugeLumi.setThreshold(MaiorLumi);
//                    //ajustando o combo box
//                    LocalDateTime hora = LocalDateTime.now();
//                    DateTimeFormatter formatterHR = DateTimeFormatter.ofPattern("HH:mm:ss");
//                    String horaFormatado = hora.format(formatterHR);
//                    lbHoraTimeLine.setText(horaFormatado);
//    }
