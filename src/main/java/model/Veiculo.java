/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author deinfo
 */
public class Veiculo {

    private String placa;
    private String horaEntrada;
    private String horaSaida;
    private double distancia;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public String getDistanciaF() {
        return String.format("%.2f",distancia);
    }
    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
    
    public String getVelocidadeF(){
       return String.format("%.2f",this.getVelocidade());
    }
    
    public double getVelocidade(){
       return ((this.getDistancia())/(this.getTempo()));
    }

    public double getTempo(){
       String[] partesHoraEntrada = this.getHoraEntrada().split(":");
       String[] partesHoraSaida = this.getHoraSaida().split(":");
       double tempoSegundos = Double.valueOf(partesHoraSaida[2]) - Double.valueOf(partesHoraEntrada[2]);
       double tempoMinutos = (Double.valueOf(partesHoraSaida[1]) - Double.valueOf(partesHoraEntrada[1]))*60;
       double tempoHoras = (Double.valueOf(partesHoraSaida[0]) - Double.valueOf(partesHoraEntrada[0]))*360;
       
       return tempoSegundos+tempoMinutos+tempoHoras;
    }
    
    public Veiculo(String placa, String horaEntrada, String horaSaida, double distancia) {
        setPlaca(placa);
        setHoraEntrada(horaEntrada);
        setHoraSaida(horaSaida);
        setDistancia(distancia);
        
    }

    public Veiculo(String placa, String horaEntrada, double distancia) {
        setPlaca(placa);
        setHoraEntrada(horaEntrada);
        setDistancia(distancia);
    }
    
    
    
    
}
