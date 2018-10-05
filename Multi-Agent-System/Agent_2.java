package student;

import mas.agents.AbstractAgent;
import mas.agents.Message;
import mas.agents.SimulationApi;
import mas.agents.StringMessage;
import mas.agents.task.mining.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Agent extends AbstractAgent {
    public Agent(int id, InputStream is, OutputStream os, SimulationApi api) throws IOException, InterruptedException {
        super(id, is, os, api);
    }

    // See also class StatusMessage
    public static String[] types = {
            "", "obstacle", "depot", "gold", "agent"
    };

    public int[][] posotrosagentes = new int[4][3];
    @Override
    public void act() throws Exception {

        StatusMessage status = sense();
        int[][] posagentesant = new int[4][3];
        int[][] posagentesnvo = new int[4][3];
        int[][] matriz_ubicaciones = new int[status.width][status.height];
        int[] pos_depot = new int[3];
        int[][] pos_oro = new int[100][5];
        int primera=0;

        char[][] m_sumresta = new char[8][2];
        m_sumresta[0][0]='s'; m_sumresta[0][1]='n'; m_sumresta[1][0]='n'; m_sumresta[1][1]='s';
        m_sumresta[2][0]='s'; m_sumresta[2][1]='s'; m_sumresta[3][0]='r'; m_sumresta[3][1]='n';
        m_sumresta[4][0]='n'; m_sumresta[4][1]='r'; m_sumresta[5][0]='r'; m_sumresta[5][1]='r';
        m_sumresta[6][0]='r'; m_sumresta[6][1]='s'; m_sumresta[7][0]='s'; m_sumresta[7][1]='r';




        int [] direcciones_ayuda = new int[status.width*status.height];
        Arrays.fill(direcciones_ayuda, 5);

        int [] direcciones_tranca = new int[status.width*status.height];
        Arrays.fill(direcciones_tranca, 5);
        int [] obstaculo_actual = new int[2];
        obstaculo_actual[0]=status.width+2;
        obstaculo_actual[1]=status.height+2;
        int voy_direccion = 0;
        int voy_direccion_ayuda = 0;
        int  estoy_ayudando = 0;

        int[][] posoro = new int[100][4];
        int cantposoro = 0;
        int buscaractual = 0;
        int[] posdepot = new int[3];
        int[] traeragente = new int [4];
        int esperando_recoger_oro = 0;

        while(true) {

            while (messageAvailable()) {
                Message m = readMessage();

                log("I have received " + m.stringify() + " de " + m.getSender());
                String[] parts = m.stringify().split(",");

                int foo0 = Integer.parseInt(parts[0]);
                //recibo las posiciones de los otros
                if(foo0==0) {

                    int foo = Integer.parseInt(parts[1]);
                    int foo2 = Integer.parseInt(parts[2]);

                   // log(String.format("RECIBI POS AGENTE  %d,%d  DE %d .",foo, foo2,m.getSender()));
                    posotrosagentes[m.getSender() - 1][0] = foo;
                    posotrosagentes[m.getSender() - 1][1] = foo2;
                    posotrosagentes[m.getSender() - 1][2] = m.getSender();

                }else{
                    if(foo0==1) {

                        log(String.format("DIRECCION AYUDA CUANDO RECIBE ALGO: %d %d %d %d %d %d",direcciones_ayuda[0],direcciones_ayuda[1],direcciones_ayuda[2]
                                ,direcciones_ayuda[3],direcciones_ayuda[4],direcciones_ayuda[5],direcciones_ayuda[6],direcciones_ayuda[7]
                                ,direcciones_ayuda[8],direcciones_ayuda[9],direcciones_ayuda[10],direcciones_ayuda[11],direcciones_ayuda[12]));



                        if(direcciones_ayuda[0]==5) {
                            voy_direccion_ayuda = 0;
                            estoy_ayudando = 1;
                            int llegarx = Integer.parseInt(parts[1]);
                            int llegary = Integer.parseInt(parts[2]);
                            log(String.format("quiero ir a [%d,%d] y estoy en [%d,%d]", llegarx, llegary, status.agentX, status.agentY));
                            int cantx = llegarx - status.agentX;
                            int canty = llegary - status.agentY;
                            int dirx = 0; //derecha + right
                            int diry = 0;//baja + down
                            if (cantx < 0) {
                                dirx = 1;
                                cantx = Math.abs(cantx);
                            }
                            if (canty < 0) {
                                diry = 1;
                                canty = Math.abs(canty);
                            }
                            int i = 0;

                            log(String.format("cantidadess: %d %d", cantx, canty));
                            if (cantx > 0) {

                                for (int j = 0; j < cantx; j++) {

                                    if (dirx == 1) {
                                        // status = left();
                                        direcciones_ayuda[i] = 0;
                                    } else {
                                        if (dirx == 0) {
                                            // status = right();
                                            direcciones_ayuda[i] = 1;

                                        }
                                    }
                                    log(String.format("PRIMERP. DATOS DIRECCION ayudaaaaa: %d", direcciones_ayuda[i]));
                                    i++;

                                }

                            }
                            if (canty > 0) {
                                for (int j = 0; j < canty; j++) {
                                    if (diry == 1) {
                                        //status = up();
                                        direcciones_ayuda[i] = 2;
                                    } else {
                                        if (diry == 0) {
                                            // status = down();
                                            direcciones_ayuda[i] = 3;
                                        }
                                    }
                                    log(String.format("SEGUNDO. DATOS DIRECCION ayudaaaaa: %d", direcciones_ayuda[i]));
                                    i++;
                                }

                            }
                            direcciones_ayuda[i + 1] = 5;
                            log(String.format("2. DATOS DIRECCION: %d %d %d %d %d %d %d %d %d %d %d %d %d"
                                    , direcciones_ayuda[0], direcciones_ayuda[1], direcciones_ayuda[2]
                                    , direcciones_ayuda[3], direcciones_ayuda[4], direcciones_ayuda[5], direcciones_ayuda[6], direcciones_ayuda[7]
                                    , direcciones_ayuda[8], direcciones_ayuda[9], direcciones_ayuda[10], direcciones_ayuda[11], direcciones_ayuda[12]));
                            status = sense();
                        }
                    }else{
                        if(foo0==2){

                            int foo = Integer.parseInt(parts[1]);
                            int foo2 = Integer.parseInt(parts[2]);
                            cantposoro = Integer.parseInt(parts[3]);
                            buscaractual = Integer.parseInt(parts[4]);

                            log(String.format("RECIBI POS OROOOO  %d,%d  Y %d  %d .",foo, foo2, cantposoro, buscaractual));
                            int  yaesta = 0;

                            for(int i=0; i<cantposoro; i++){
                               // log(String.format("RECORRE ORO: %d y   %d , %d",i, posoro[i][0],posoro[i][1]));
                                if(posoro[i][0]==foo && posoro[i][1]==foo2 && posoro[i][3]==0){
                                    yaesta = 1;
                                    break;
                                }
                            }

                            if(yaesta==0) {
                                log(String.format("1. GUARDOOOO POS OROOOO  %d,%d  Y %d ESTOY BUSCANDO: %d .", foo, foo2, cantposoro, buscaractual));
                                posoro[cantposoro][0] = foo;
                                posoro[cantposoro][1] = foo2;
                                posoro[cantposoro][2] = 1;
                                cantposoro++;
                            }

                        }else{
                            if(foo0==3){
                                int foo = Integer.parseInt(parts[1]);
                                int foo2 = Integer.parseInt(parts[2]);
                                int foo3 = Integer.parseInt(parts[3]);
                                posoro[buscaractual][foo2]=foo3;
                                posoro[buscaractual][3]=1;
                            }

                        }

                    }
                }
            }

            log(String.format("I am now on position [%d,%d] of a %dx%d map.",
                    status.agentX, status.agentY, status.width, status.height));
            matriz_ubicaciones[status.agentY][status.agentX]=5;


            posotrosagentes[getAgentId()-1][0]=status.agentX;
            posotrosagentes[getAgentId()-1][1]=status.agentY;
            posotrosagentes[getAgentId()-1][2]=getAgentId();

            //mando mi posicion

            String datostr =new String( "0," +status.agentX + "," + status.agentY);

            if(getAgentId()==1){
                sendMessage(2, new StringMessage(datostr));
                sendMessage(3, new StringMessage(datostr));
                sendMessage(4, new StringMessage(datostr));
            }else{
                if(getAgentId()==2){
                    sendMessage(1, new StringMessage(datostr));
                    sendMessage(3, new StringMessage(datostr));
                    sendMessage(4, new StringMessage(datostr));
                }else{
                    if(getAgentId()==3){
                        sendMessage(2, new StringMessage(datostr));
                        sendMessage(1, new StringMessage(datostr));
                        sendMessage(4, new StringMessage(datostr));
                    }else{
                        if(getAgentId()==4){
                            sendMessage(2, new StringMessage(datostr));
                            sendMessage(3, new StringMessage(datostr));
                            sendMessage(1, new StringMessage(datostr));
                        }else{

                        }
                    }
                }
            }


            for(StatusMessage.SensorData data : status.sensorInput) {
                log(String.format("I see %s at [%d,%d]", types[data.type], data.x, data.y));
                if(data.type==1){//obstacle
                    matriz_ubicaciones[data.y][data.x]=1;
                    log(String.format("entro a obstaculo [%d,%d]", data.x, data.y));
                }else{
                    if(data.type==2){//depot
                        matriz_ubicaciones[data.y][data.x]=2;

                        posdepot[0]=data.x;
                        posdepot[1]=data.y;
                        posdepot[2]=1;

                        String datostr2 = new String("4," + data.x + "," + data.y);

                        if(getAgentId()==1){
                            sendMessage(2, new StringMessage(datostr2));
                            sendMessage(3, new StringMessage(datostr2));
                            sendMessage(4, new StringMessage(datostr2));
                        }else{
                            if(getAgentId()==2){
                                sendMessage(1, new StringMessage(datostr2));
                                sendMessage(3, new StringMessage(datostr2));
                                sendMessage(4, new StringMessage(datostr2));
                            }else{
                                if(getAgentId()==3){
                                    sendMessage(2, new StringMessage(datostr2));
                                    sendMessage(1, new StringMessage(datostr2));
                                    sendMessage(4, new StringMessage(datostr2));
                                }else{
                                    if(getAgentId()==4){
                                        sendMessage(2, new StringMessage(datostr2));
                                        sendMessage(3, new StringMessage(datostr2));
                                        sendMessage(1, new StringMessage(datostr2));
                                    }else{

                                    }
                                }
                            }
                        }
                        log(String.format("VEO DEPOSITO  %d , %d  y  %d, EN: %d,%d", data.x, data.y,posoro[buscaractual][3], status.agentX, status.agentY));


                        if(posoro[buscaractual][3]==1){
                            log(String.format("EMTRO PRIMER IDFFF"));

                            if(data.y!=status.agentY && data.x!=status.agentX){
                                log(String.format("EMTRO SEGUNDO IDFFF"));
                                if(data.y-status.agentY>0 && data.y-status.agentY>0){
                                    log(String.format("EMTRO 3 IDFFF"));

                                    voy_direccion=0;
                                    direcciones_tranca[0]=3;
                                    direcciones_tranca[1]=4;
                                    direcciones_tranca[2]=4;
                                    direcciones_tranca[3]=4;
                                    direcciones_tranca[4]=4;
                                    direcciones_tranca[5]=5;
                                }else{
                                    if(data.y-status.agentY<0 && data.y-status.agentY<0){
                                        log(String.format("EMTRO 4 IDFFF"));

                                        voy_direccion=0;
                                        direcciones_tranca[0]=2;
                                        direcciones_tranca[1]=4;
                                        direcciones_tranca[2]=4;
                                        direcciones_tranca[3]=4;
                                        direcciones_tranca[4]=4;
                                        direcciones_tranca[5]=5;
                                    }else{
                                        if(data.y-status.agentY>0 && data.y-status.agentY<0){
                                            log(String.format("EMTRO 5 IDFFF"));

                                            voy_direccion=0;
                                            direcciones_tranca[0]=3;
                                            direcciones_tranca[1]=4;
                                            direcciones_tranca[2]=4;
                                            direcciones_tranca[3]=4;
                                            direcciones_tranca[4]=4;
                                            direcciones_tranca[5]=5;
                                        }else{
                                            if(data.y-status.agentY<0 && data.y-status.agentY>0){
                                                log(String.format("EMTRO 6 IDFFF"));
                                                voy_direccion=0;
                                                direcciones_tranca[0]=2;
                                                direcciones_tranca[1]=4;
                                                direcciones_tranca[2]=4;
                                                direcciones_tranca[3]=4;
                                                direcciones_tranca[4]=4;
                                                direcciones_tranca[5]=5;
                                            }
                                        }
                                    }
                                }

                            }else{
                                if(data.x-status.agentY<0){
                                    log(String.format("EMTRO 7 IDFFF"));
                                    voy_direccion=0;
                                    direcciones_tranca[0]=3;
                                    direcciones_tranca[1]=4;
                                    direcciones_tranca[2]=4;
                                    direcciones_tranca[3]=4;
                                    direcciones_tranca[4]=4;
                                    direcciones_tranca[5]=5;
                                }else{
                                    if(data.x-status.agentY>0){
                                        log(String.format("EMTRO 8 IDFFF"));
                                        voy_direccion=0;
                                        direcciones_tranca[0]=3;
                                        direcciones_tranca[1]=4;
                                        direcciones_tranca[2]=4;
                                        direcciones_tranca[3]=4;
                                        direcciones_tranca[4]=4;
                                        direcciones_tranca[5]=5;
                                    }
                                }

                            }


                            if(status.agentX==data.x && status.agentY==data.y) {
                                log(String.format("ESTOY EN DEPOSITO Y PAROOOOO "));

                                int datossprov = posagentesnvo[getAgentId()-1][2];
                                posagentesnvo[getAgentId() - 1][2] = 4;//cuando encuentro DEPOT paro


                                drop();
                                log(String.format("Hizo drop"));

                                posagentesnvo[getAgentId()-1][2] = datossprov;
                                posoro[buscaractual][3]=2;
                                buscaractual++;
                                voy_direccion=0;
                                Arrays.fill(direcciones_tranca, 5);



                                if(posoro[buscaractual][2]!=0){
                                    posagentesnvo[getAgentId() - 1][2] = 4;
                                    log(String.format("HAY OTRO OROOOOOOOO"));
                                    log(String.format("Tengo que ir a %d,%d y estoy en [%d,%d]",posoro[buscaractual][0],posoro[buscaractual][1]
                                            , status.agentX, status.agentY));
                                    voy_direccion=0;

                                    int cantx = posoro[buscaractual][0] - status.agentX;
                                    int canty = posoro[buscaractual][1] - status.agentY;
                                    int dirx = 0; //derecha + right
                                    int diry = 0;//baja + down
                                    if (cantx < 0) {
                                        dirx = 1;
                                        cantx = Math.abs(cantx);
                                    }
                                    if (canty < 0) {
                                        diry = 1;
                                        canty = Math.abs(canty);
                                    }
                                    int i = 0;

                                    log(String.format("cantidadess: %d %d", cantx, canty));
                                    if (cantx > 0) {

                                        for (int j = 0; j < cantx; j++) {

                                            if (dirx == 1) {
                                                // status = right();
                                                direcciones_tranca[i] = 0;
                                            } else {
                                                if (dirx == 0) {
                                                    // status = left();
                                                    direcciones_tranca[i] = 1;

                                                }
                                            }
                                            log(String.format("PRIMERo. DATOS IR POR OTRO OROOOO: %d", direcciones_tranca[i]));
                                            i++;

                                        }

                                    }
                                    if (canty > 0) {
                                        for (int j = 0; j < canty; j++) {
                                            if (diry == 1) {
                                                //status = up();
                                                direcciones_tranca[i] = 2;
                                            } else {
                                                if (diry == 0) {
                                                    // status = down();
                                                    direcciones_tranca[i] = 3;
                                                }
                                            }
                                            log(String.format("SEGUNDO. DATOS IR POR OTRO OROOOO: %d", direcciones_tranca[i]));
                                            i++;
                                        }

                                    }
                                    direcciones_tranca[i] = 5;


                                }
                            }

                        }



                    }else{
                        if(data.type==3){//gold
                            matriz_ubicaciones[data.y][data.x]=3;
                            log(String.format("VEOOOO ORO: %d y %d ESTOY EN: %d y %d",data.y, data.x, status.agentY, status.agentX));
                            if(cantposoro>0){
                                int yaesta = 0;
                                int identyaesta = 0;

                                for(int i=0; i<cantposoro; i++){
                                    log(String.format("RECORRE ORO: %d y   %d vs %d , %d  vs %d",i, posoro[i][0],data.x,posoro[i][1],data.y));
                                    if(posoro[i][0]==data.x && posoro[i][1]==data.y){ // && posoro[i][3]==0
                                        yaesta = 1;
                                        identyaesta = i;

                                        break;
                                    }
                                }
                                if(yaesta==0){ //es oro nuevo
                                    posoro[cantposoro][0]=data.x;
                                    posoro[cantposoro][1]=data.y;
                                    posoro[cantposoro][2]=1;
                                    log(String.format("1. ENCONTRO ORO NUEVO %d",cantposoro));

                                    log(String.format("2. GUARDOOOO POS OROOOO  %d,%d  Y %d ESTOY BUSCANDO: %d .",data.x, data.y, cantposoro, buscaractual));


                                    //mando el oro pa todos

                                    String datosoro =new String( "2," +data.x + "," + data.y + "," + cantposoro+ "," + buscaractual);
                                    if(getAgentId()==1){
                                        sendMessage(2, new StringMessage(datosoro));
                                        sendMessage(3, new StringMessage(datosoro));
                                        sendMessage(4, new StringMessage(datosoro));
                                    }else{
                                        if(getAgentId()==2){
                                            sendMessage(1, new StringMessage(datosoro));
                                            sendMessage(3, new StringMessage(datosoro));
                                            sendMessage(4, new StringMessage(datosoro));
                                        }else{
                                            if(getAgentId()==3){
                                                sendMessage(2, new StringMessage(datosoro));
                                                sendMessage(1, new StringMessage(datosoro));
                                                sendMessage(4, new StringMessage(datosoro));
                                            }else{
                                                if(getAgentId()==4){
                                                    sendMessage(2, new StringMessage(datosoro));
                                                    sendMessage(3, new StringMessage(datosoro));
                                                    sendMessage(1, new StringMessage(datosoro));
                                                }else{

                                                }
                                            }
                                        }
                                    }
                                    cantposoro++;

                                    if(posoro[buscaractual][2]==1 && posoro[buscaractual][0]==data.x && posoro[buscaractual][1]==data.y) { //posoro[buscaractual][3]==3
                                        log(String.format("INGRESOOO A CUANDO ENCONTRO ORO, primero: %d, y %d,%d  vs %d,%d ",posoro[buscaractual][2],data.x,data.y
                                        ,posoro[buscaractual][0],posoro[buscaractual][1]));
                                        if(status.agentX==data.x && status.agentY==data.y) {
                                            log(String.format("ENCONTRO ORO 2 Y PAROOOOO " ));
                                            esperando_recoger_oro=1;
                                            posagentesnvo[getAgentId()-1][2]=4;//cuando encuentro oro paro
                                            int[][] compposoro = new int[4][3];
                                            int[] totalescompa = new int[2];
                                            totalescompa[0]=23;
                                            totalescompa[1]=0;

                                            int direccionact=0;
                                            int direccionact_prov=0;
                                            for (int i = 0; i < 4; i++) { //recorro las posiciones de los otros pa ver cual es el mas cerk
                                                if (posotrosagentes[i][2] != getAgentId()) {
                                                    compposoro[i][0] = Math.abs(posotrosagentes[i][0] - data.x);
                                                    compposoro[i][1] = Math.abs(posotrosagentes[i][1] - data.y);
                                                    compposoro[i][2] = posotrosagentes[i][2];
                                                    int sumacoma = compposoro[i][0] + compposoro[i][1];
                                                    if(posotrosagentes[i][0] - data.x<0){
                                                        direccionact_prov=1;
                                                    }else{
                                                        direccionact_prov=2;
                                                    }
                                                    if (totalescompa[0] > sumacoma) {
                                                        totalescompa[0] = sumacoma;
                                                        totalescompa[1] = compposoro[i][2];
                                                        direccionact = direccionact_prov;
                                                    }
                                                }
                                            }
                                            log(String.format("2. EL MAS CERCANO ES %d ", totalescompa[1]));
                                            if ((Math.abs(posotrosagentes[totalescompa[1] - 1][0] - posotrosagentes[getAgentId() - 1][0]) +
                                                    Math.abs(data.y - posotrosagentes[totalescompa[1] - 1][1]))
                                                    <
                                                    (Math.abs(posotrosagentes[totalescompa[1] - 1][0] - data.x) +
                                                            Math.abs(posotrosagentes[getAgentId() - 1][1] - posotrosagentes[totalescompa[1] - 1][1]))) {

                                                // log(String.format("LAS COORDENADAS PA LLEGAR SON %d,%d ", posotrosagentes[getAgentId()-1][0], data.y ));
                                                if(direccionact==1){
                                                    traeragente[0] = posotrosagentes[getAgentId() - 1][0]-1;
                                                }else{
                                                    traeragente[0] = posotrosagentes[getAgentId() - 1][0]+1;
                                                }


                                                traeragente[1] = data.y;
                                                traeragente[2] = 1;
                                                traeragente[3] = totalescompa[1];
                                                String datostr2 = new String("1," + traeragente[0] + "," + traeragente[1]);
                                                sendMessage(totalescompa[1], new StringMessage(datostr2));
                                                log(String.format("EN LOG 3 ENVIA envioo: %d y   %d , %d", totalescompa[1], traeragente[0],
                                                        traeragente[1]));

                                            } else {

                                                if(direccionact==1){
                                                    traeragente[0] = posotrosagentes[getAgentId() - 1][0]-1;
                                                }else{
                                                    traeragente[0] = posotrosagentes[getAgentId() - 1][0]+1;
                                                }

                                                traeragente[1] = posotrosagentes[getAgentId() - 1][1];
                                                log(String.format("DATO PA AGENTE SON %d - %d ", traeragente[0],  traeragente[1]));
                                                traeragente[2] = 1;
                                                traeragente[3] = totalescompa[1];
                                                String datostr2 = new String("1," + traeragente[0] + "," + traeragente[1]);
                                                sendMessage(totalescompa[1], new StringMessage(datostr2));
                                                log(String.format("EN LOG 7 ENVIA  %d y   %d , %d", totalescompa[1], traeragente[0], traeragente[1]));

                                            }
                                        }else{
                                            if(data.y!=status.agentY && data.x!=status.agentX){
                                                if(data.y-status.agentY>0 && data.y-status.agentY>0){
                                                    voy_direccion=0;
                                                    direcciones_tranca[0]=3;
                                                    direcciones_tranca[1]=4;
                                                    direcciones_tranca[2]=4;
                                                    direcciones_tranca[3]=4;
                                                    direcciones_tranca[4]=4;
                                                    direcciones_tranca[5]=5;
                                                }else{
                                                    if(data.y-status.agentY<0 && data.y-status.agentY<0){
                                                        voy_direccion=0;
                                                        direcciones_tranca[0]=2;
                                                        direcciones_tranca[1]=4;
                                                        direcciones_tranca[2]=4;
                                                        direcciones_tranca[3]=4;
                                                        direcciones_tranca[4]=4;
                                                        direcciones_tranca[5]=5;
                                                    }else{
                                                        if(data.y-status.agentY>0 && data.y-status.agentY<0){
                                                            voy_direccion=0;
                                                            direcciones_tranca[0]=3;
                                                            direcciones_tranca[1]=4;
                                                            direcciones_tranca[2]=4;
                                                            direcciones_tranca[3]=4;
                                                            direcciones_tranca[4]=4;
                                                            direcciones_tranca[5]=5;
                                                        }else{
                                                            if(data.y-status.agentY<0 && data.y-status.agentY>0){
                                                                voy_direccion=0;
                                                                direcciones_tranca[0]=2;
                                                                direcciones_tranca[1]=4;
                                                                direcciones_tranca[2]=4;
                                                                direcciones_tranca[3]=4;
                                                                direcciones_tranca[4]=4;
                                                                direcciones_tranca[5]=5;
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }

                                    }
                                }else{

                                    log(String.format("ENTROOO AL ELSE NO ES ORO NUEVO y %d", posoro[buscaractual][2], buscaractual));

                                    log(String.format("1. INGRESOOO A CUANDO ENCONTRO ORO, primero: %d, y %d,%d  vs %d,%d ",posoro[buscaractual][2],data.x,data.y
                                            ,posoro[buscaractual][0],posoro[buscaractual][1]));



                                    if(posoro[buscaractual][2]==1 && posoro[buscaractual][0]==data.x && posoro[buscaractual][1]==data.y) {
                                        if(status.agentX==data.x && status.agentY==data.y) {
                                            log(String.format("ENCONTRO ORO Y PAROOOOO " ));
                                            esperando_recoger_oro=1;
                                            posagentesnvo[getAgentId()-1][2]=4;//cuando encuentro oro paro
                                            int[][] compposoro = new int[4][3];
                                            int[] totalescompa = new int[2];
                                            totalescompa[0]=23;
                                            totalescompa[1]=0;

                                            int direccionact=0;
                                            int direccionact_prov=0;
                                            for (int i = 0; i < 4; i++) { //recorro las posiciones de los otros pa ver cual es el mas cerk
                                                if (posotrosagentes[i][2] != getAgentId()) {
                                                    compposoro[i][0] = Math.abs(posotrosagentes[i][0] - data.x);
                                                    compposoro[i][1] = Math.abs(posotrosagentes[i][1] - data.y);
                                                    compposoro[i][2] = posotrosagentes[i][2];
                                                    int sumacoma = compposoro[i][0] + compposoro[i][1];
                                                    if(posotrosagentes[i][0] - data.x<0){
                                                        direccionact_prov=1;
                                                    }else{
                                                        direccionact_prov=2;
                                                    }
                                                    if (totalescompa[0] > sumacoma) {
                                                        totalescompa[0] = sumacoma;
                                                        totalescompa[1] = compposoro[i][2];
                                                        direccionact = direccionact_prov;
                                                    }
                                                }
                                            }
                                            log(String.format("2. EL MAS CERCANO ES %d ", totalescompa[1]));
                                            if ((Math.abs(posotrosagentes[totalescompa[1] - 1][0] - posotrosagentes[getAgentId() - 1][0]) +
                                                    Math.abs(data.y - posotrosagentes[totalescompa[1] - 1][1]))
                                                    <
                                                    (Math.abs(posotrosagentes[totalescompa[1] - 1][0] - data.x) +
                                                            Math.abs(posotrosagentes[getAgentId() - 1][1] - posotrosagentes[totalescompa[1] - 1][1]))) {

                                                // log(String.format("LAS COORDENADAS PA LLEGAR SON %d,%d ", posotrosagentes[getAgentId()-1][0], data.y ));
                                                if(direccionact==1){
                                                    traeragente[0] = posotrosagentes[getAgentId() - 1][0]-1;
                                                }else{
                                                    traeragente[0] = posotrosagentes[getAgentId() - 1][0]+1;
                                                }


                                                traeragente[1] = data.y;
                                                traeragente[2] = 1;
                                                traeragente[3] = totalescompa[1];
                                                String datostr2 = new String("1," + traeragente[0] + "," + traeragente[1]);
                                                sendMessage(totalescompa[1], new StringMessage(datostr2));
                                                log(String.format("EN LOG 3 ENVIA envioo: %d y   %d , %d", totalescompa[1], traeragente[0],
                                                        traeragente[1]));

                                            } else {

                                                if(direccionact==1){
                                                    traeragente[0] = posotrosagentes[getAgentId() - 1][0]-1;
                                                }else{
                                                    traeragente[0] = posotrosagentes[getAgentId() - 1][0]+1;
                                                }

                                                traeragente[1] = posotrosagentes[getAgentId() - 1][1];
                                                log(String.format("DATO PA AGENTE SON %d - %d ", traeragente[0],  traeragente[1]));
                                                traeragente[2] = 1;
                                                traeragente[3] = totalescompa[1];
                                                String datostr2 = new String("1," + traeragente[0] + "," + traeragente[1]);
                                                sendMessage(totalescompa[1], new StringMessage(datostr2));
                                                log(String.format("EN LOG 7 ENVIA  %d y   %d , %d", totalescompa[1], traeragente[0], traeragente[1]));

                                            }
                                        }else{
                                            if(data.y!=status.agentY && data.x!=status.agentX){
                                                if(data.y-status.agentY>0 && data.y-status.agentY>0){
                                                    voy_direccion=0;
                                                    direcciones_tranca[0]=3;
                                                    direcciones_tranca[1]=4;
                                                    direcciones_tranca[2]=4;
                                                    direcciones_tranca[3]=4;
                                                    direcciones_tranca[4]=4;
                                                    direcciones_tranca[5]=5;
                                                }else{
                                                    if(data.y-status.agentY<0 && data.y-status.agentY<0){
                                                        voy_direccion=0;
                                                        direcciones_tranca[0]=2;
                                                        direcciones_tranca[1]=4;
                                                        direcciones_tranca[2]=4;
                                                        direcciones_tranca[3]=4;
                                                        direcciones_tranca[4]=4;
                                                        direcciones_tranca[5]=5;
                                                    }else{
                                                        if(data.y-status.agentY>0 && data.y-status.agentY<0){
                                                            voy_direccion=0;
                                                            direcciones_tranca[0]=3;
                                                            direcciones_tranca[1]=4;
                                                            direcciones_tranca[2]=4;
                                                            direcciones_tranca[3]=4;
                                                            direcciones_tranca[4]=4;
                                                            direcciones_tranca[5]=5;
                                                        }else{
                                                            if(data.y-status.agentY<0 && data.y-status.agentY>0){
                                                                voy_direccion=0;
                                                                direcciones_tranca[0]=2;
                                                                direcciones_tranca[1]=4;
                                                                direcciones_tranca[2]=4;
                                                                direcciones_tranca[3]=4;
                                                                direcciones_tranca[4]=4;
                                                                direcciones_tranca[5]=5;
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }


                                    }
                                }
                            }else{


                                posoro[cantposoro][0]=data.x;
                                posoro[cantposoro][1]=data.y;
                                posoro[cantposoro][2]=1;

                                log(String.format("3. GUARDOOOO POS OROOOO  %d,%d  Y %d ESTOY BUSCANDO: %d .",data.x, data.y, cantposoro, buscaractual));


                                log(String.format("1. ENCONTRO EL PRIMER ORO %d",cantposoro));
                                if(data.y!=status.agentY && data.x!=status.agentX){
                                    log(String.format("SDFSDF %d",cantposoro));
                                    if(data.x-status.agentX>0 && data.y-status.agentY>0){
                                        voy_direccion=0;
                                        direcciones_tranca[0]=3;
                                        direcciones_tranca[1]=1;
                                        direcciones_tranca[2]=4;
                                        direcciones_tranca[3]=4;
                                        direcciones_tranca[4]=4;
                                        direcciones_tranca[5]=5;
                                    }else{
                                        if(data.x-status.agentX<0 && data.y-status.agentY<0){
                                            voy_direccion=0;
                                            direcciones_tranca[0]=2;
                                            direcciones_tranca[1]=0;
                                            direcciones_tranca[2]=4;
                                            direcciones_tranca[3]=4;
                                            direcciones_tranca[4]=4;
                                            direcciones_tranca[5]=5;
                                        }else{
                                            if(data.x-status.agentX>0 && data.y-status.agentY<0){
                                                voy_direccion=0;
                                                direcciones_tranca[0]=1;
                                                direcciones_tranca[1]=2;
                                                direcciones_tranca[2]=4;
                                                direcciones_tranca[3]=4;
                                                direcciones_tranca[4]=4;
                                                direcciones_tranca[5]=5;
                                            }else{
                                                if(data.x-status.agentX<0 && data.y-status.agentY>0){
                                                    voy_direccion=0;
                                                    direcciones_tranca[0]=3;
                                                    direcciones_tranca[1]=0;
                                                    direcciones_tranca[2]=4;
                                                    direcciones_tranca[3]=4;
                                                    direcciones_tranca[4]=4;
                                                    direcciones_tranca[5]=5;
                                                }
                                            }
                                        }
                                    }

                                }else{

                                }


                                //mando el oro pa todos

                                String datosoro =new String( "2," +data.x + "," + data.y+ "," + cantposoro+ "," + buscaractual);

                                if(getAgentId()==1){
                                    sendMessage(2, new StringMessage(datosoro));
                                    sendMessage(3, new StringMessage(datosoro));
                                    sendMessage(4, new StringMessage(datosoro));
                                }else{
                                    if(getAgentId()==2){
                                        sendMessage(1, new StringMessage(datosoro));
                                        sendMessage(3, new StringMessage(datosoro));
                                        sendMessage(4, new StringMessage(datosoro));
                                    }else{
                                        if(getAgentId()==3){
                                            sendMessage(2, new StringMessage(datosoro));
                                            sendMessage(1, new StringMessage(datosoro));
                                            sendMessage(4, new StringMessage(datosoro));
                                        }else{
                                            if(getAgentId()==4){
                                                sendMessage(2, new StringMessage(datosoro));
                                                sendMessage(3, new StringMessage(datosoro));
                                                sendMessage(1, new StringMessage(datosoro));
                                            }else{

                                            }
                                        }
                                    }
                                }
                                cantposoro++;



                                if(status.agentX==data.x && status.agentY==data.y) {
                                    log(String.format("ENCONTRO ORO Y PAROOOOO " ));
                                    esperando_recoger_oro=1;
                                    posagentesnvo[getAgentId()-1][2]=4;//cuando encuentro oro paro
                                    int[][] compposoro = new int[4][3];
                                    int[] totalescompa = new int[2];
                                    totalescompa[0]=23;
                                    totalescompa[1]=0;


                                    for (int i = 0; i < 4; i++) { //recorro las posiciones de los otros pa ver cual es el mas cerk
                                        if (posotrosagentes[i][2] != getAgentId()) {
                                            compposoro[i][0] = Math.abs(posotrosagentes[i][0] - data.x);
                                            compposoro[i][1] = Math.abs(posotrosagentes[i][1] - data.y);
                                            compposoro[i][2] = posotrosagentes[i][2];
                                            int sumacoma = compposoro[i][0] + compposoro[i][1];

                                            if (totalescompa[0] > sumacoma) {
                                                totalescompa[0] = sumacoma;
                                                totalescompa[1] = compposoro[i][2];
                                            }
                                        }
                                    }
                                    log(String.format("3. EL MAS CERCANO ES %d ", totalescompa[1]));
                                    if ((Math.abs(posotrosagentes[totalescompa[1] - 1][0] - posotrosagentes[getAgentId() - 1][0]) + Math.abs(data.y - posotrosagentes[totalescompa[1] - 1][1]))
                                            <
                                            (Math.abs(posotrosagentes[totalescompa[1] - 1][0] - data.x) + Math.abs(posotrosagentes[getAgentId() - 1][1] - posotrosagentes[totalescompa[1] - 1][1]))) {

                                        // log(String.format("LAS COORDENADAS PA LLEGAR SON %d,%d ", posotrosagentes[getAgentId()-1][0], data.y ));
                                        traeragente[0] = posotrosagentes[getAgentId() - 1][0];
                                        traeragente[1] = data.y;
                                        traeragente[2] = 1;
                                        traeragente[3] = totalescompa[1];
                                        String datostr2 = new String("1," + posotrosagentes[getAgentId() - 1][0] + "," + data.y);
                                        sendMessage(totalescompa[1], new StringMessage(datostr2));
                                        log(String.format("EN LOG 4 ENVIA envioo: %d y   %d , %d", totalescompa[1], posotrosagentes[getAgentId() - 1][0],
                                                data.y));

                                    } else {
                                        // log(String.format("LAS COORDENADAS PA LLEGAR SON %d,%d ", data.x,  posotrosagentes[getAgentId()-1][1]));
                                        traeragente[0] = data.x;
                                        traeragente[1] = posotrosagentes[getAgentId() - 1][1];
                                        traeragente[2] = 1;
                                        traeragente[3] = totalescompa[1];
                                        String datostr2 = new String("1," + data.x + "," + posotrosagentes[getAgentId() - 1][1]);
                                        sendMessage(totalescompa[1], new StringMessage(datostr2));
                                        log(String.format("EN LOG 5 ENVIA  %d y   %d , %d", totalescompa[1], data.x, data.x, posotrosagentes[getAgentId() - 1][1]));

                                    }
                                }




                            }








                        }else{
                            if(data.type==4){//agent
                                matriz_ubicaciones[data.y][data.x]=4;


                                //   posotrosagentes[0][0]=data.x;
                                //   posotrosagentes[0][1]=data.y;

                                log(String.format("VEOO AGENTE PA HACER PICK %d vs %d, %d vs %d, %d y %d",
                                        traeragente[0], data.x, traeragente[1],data.y, traeragente[2],  posoro[buscaractual][3] ));
                                if(traeragente[0]==data.x && traeragente[1]==data.y && traeragente[2]!=0 && posoro[buscaractual][3]==0){
                                    int datossprov = posagentesnvo[getAgentId()-1][2];
                                    pick();
                                    estoy_ayudando = 0;
                                    traeragente[0]=0;
                                    traeragente[1]=0;
                                    traeragente[2]=0;
                                    posoro[buscaractual][2]=2;
                                    posoro[buscaractual][3]=1;
                                    log(String.format("Hizo pick" ));
                                    posagentesnvo[getAgentId()-1][2] = datossprov;
                                    esperando_recoger_oro=0;
                                    log(String.format("datosss %d y %d", posoro[buscaractual][2], posoro[0][2] ));
                                    //mando el oro pa todos

                                    String datosoro =new String( "3," +buscaractual + "," + 2 + "," + 2);
                                    if(posdepot[2]==1){
                                        log(String.format("YA HAY UN DEPOSITO GUARDADOOOOOO EN: %d, %d", posdepot[0], posdepot[1]));


                                    }

                                        int posparamoversex = status.agentX - data.x;
                                        int posparamoversey = status.agentY - data.y;

                                        if(Math.abs(posparamoversex) > 0){
                                            if(status.agentY>0) {
                                                status = up();
                                                if(posparamoversex > 0){
                                                    status = left();
                                                    status = left();
                                                    status = down();
                                                    posagentesnvo[getAgentId()-1][2]= 0;

                                                }else{
                                                    status = right();
                                                    status = right();
                                                    status = down();
                                                    posagentesnvo[getAgentId()-1][2]= 1;
                                                }
                                            }else{
                                                status = down();
                                                if(posparamoversex > 0){
                                                    status = left();
                                                    status = left();
                                                    status = up();
                                                    posagentesnvo[getAgentId()-1][2]= 0;
                                                }else{
                                                    status = right();
                                                    status = right();
                                                    status = up();
                                                    posagentesnvo[getAgentId()-1][2]= 1;
                                                }

                                            }

                                        }else{
                                            if(Math.abs(posparamoversey) > 0){

                                            }
                                        }





                                    if(getAgentId()==1){
                                        sendMessage(2, new StringMessage(datosoro));
                                        sendMessage(3, new StringMessage(datosoro));
                                        sendMessage(4, new StringMessage(datosoro));
                                    }else{
                                        if(getAgentId()==2){
                                            sendMessage(1, new StringMessage(datosoro));
                                            sendMessage(3, new StringMessage(datosoro));
                                            sendMessage(4, new StringMessage(datosoro));
                                        }else{
                                            if(getAgentId()==3){
                                                sendMessage(2, new StringMessage(datosoro));
                                                sendMessage(1, new StringMessage(datosoro));
                                                sendMessage(4, new StringMessage(datosoro));
                                            }else{
                                                if(getAgentId()==4){
                                                    sendMessage(2, new StringMessage(datosoro));
                                                    sendMessage(3, new StringMessage(datosoro));
                                                    sendMessage(1, new StringMessage(datosoro));
                                                }else{

                                                }
                                            }
                                        }
                                    }


                                }else{

                                }

                            }else{

                            }
                        }
                    }
                }
            }

            //eL AGENTE 1 GUARDA LO QUE VE
            if(getAgentId()==1) {
                for (int i = 0; i < 8; i++) {
                    int xalrededor = 0;int yalrededor = 0;
                    if (m_sumresta[i][0] == 's') {
                        xalrededor = status.agentX + 1;
                    } else {
                        if (m_sumresta[i][0] == 'r') {
                            xalrededor = status.agentX - 1;
                        } else {
                            xalrededor = status.agentX;
                        }
                    }
                    if (m_sumresta[i][1] == 's') {
                        yalrededor = status.agentY + 1;
                    } else {
                        if (m_sumresta[i][1] == 'r') {
                            yalrededor = status.agentY - 1;
                        } else {
                            yalrededor = status.agentY;
                        }
                    }
                  //  log(String.format("1. ENTROO A ALREDEDOR  %d, %d   y %d", xalrededor, yalrededor, matriz_ubicaciones[status.agentX][status.agentY]));
                    if (xalrededor >= 0 && yalrededor >= 0 && xalrededor < status.height && yalrededor < status.width) {
                        if (matriz_ubicaciones[yalrededor][xalrededor] == 0) {
                            matriz_ubicaciones[yalrededor][xalrededor] = 5;
                        }
                    }

                }
            }

            //priner agente es el q busk
            if(getAgentId()==1){


                //pa saber pa que lado arrankr
                if((status.agentX==0 && status.agentY==0 || status.agentX==0 && status.agentY==11) && primera == 0){
                    posagentesnvo[getAgentId()-1][2]=1;
                    primera = 1;
                }else{
                    if((status.agentX==11 && status.agentY==11 || status.agentX==11 && status.agentY==0) && primera == 0){
                        posagentesnvo[getAgentId()-1][2]=0;
                        primera = 1;
                    }
                }
                //direcciones pa moverse
                if(posagentesnvo[getAgentId()-1][2]==0){
                    status = left();
                }else{
                    if(posagentesnvo[getAgentId()-1][2]==1){
                        status = right();
                    }else{
                        if(posagentesnvo[getAgentId()-1][2]==2){
                            status = up();
                        }else{
                            if(posagentesnvo[getAgentId()-1][2]==3){
                                status = down();
                            }else{
                                if(posagentesnvo[getAgentId()-1][2]==4){
                                    status = sense();
                                }
                            }
                        }
                    }
                }

                if(direcciones_tranca[0]!=5){
                    //direcciones pa moverse

                  //  log(String.format("MOVERSEEE EN: %d ",direcciones_tranca[voy_direccion]));
                    if(direcciones_tranca[voy_direccion]==0){
                        status = left();
                    }else{
                        if(direcciones_tranca[voy_direccion]==1){
                            status = right();
                        }else{
                            if(direcciones_tranca[voy_direccion]==2){
                                status = up();
                            }else{
                                if(direcciones_tranca[voy_direccion]==3){
                                    status = down();
                                }else{
                                    if(direcciones_tranca[voy_direccion]==4){
                                        status = sense();
                                    }
                                }
                            }
                        }
                    }

                    if(direcciones_tranca[voy_direccion]==5) {
                        posagentesnvo[getAgentId()-1][2] = direcciones_tranca[voy_direccion];
                        voy_direccion=0;
                        Arrays.fill(direcciones_tranca, 5);
                    }

                    log(String.format("DATOS DIRECCION: %d %d %d %d %d %d %d %d %d %d %d %d %d",
                            direcciones_tranca[0],direcciones_tranca[1],direcciones_tranca[2]
                            ,direcciones_tranca[3],direcciones_tranca[4],direcciones_tranca[5],direcciones_tranca[6],direcciones_tranca[7]
                            ,direcciones_tranca[8],direcciones_tranca[9],direcciones_tranca[10],direcciones_tranca[11],direcciones_tranca[12]));


                    log(String.format("1. ENTROOOOO: %d da %d ",voy_direccion,direcciones_tranca[voy_direccion]));

                        if(direcciones_tranca[voy_direccion+1]==5) {
                            posagentesnvo[getAgentId()-1][2] = direcciones_tranca[voy_direccion];
                            log(String.format("2. ENTROOOOO: %d da %d ",voy_direccion,direcciones_tranca[voy_direccion]));
                            voy_direccion=0;

                            Arrays.fill(direcciones_tranca, 5);
                        }

                    voy_direccion++;


                }

            }





            if(direcciones_ayuda[0]!=5 && posoro[buscaractual][2]!=0){
                //direcciones pa moverse


                if(direcciones_ayuda[voy_direccion_ayuda]==0){
                    status = left();
                    log(String.format("MOVERSEEE EN: %d estoy en %d ",direcciones_ayuda[voy_direccion],voy_direccion));
                }else{
                    if(direcciones_ayuda[voy_direccion_ayuda]==1){
                        status = right();
                        log(String.format("MOVERSEEE EN: %d estoy en %d ",direcciones_ayuda[voy_direccion],voy_direccion));
                    }else{
                        if(direcciones_ayuda[voy_direccion_ayuda]==2){
                            status = up();
                            log(String.format("MOVERSEEE EN: %d estoy en %d ",direcciones_ayuda[voy_direccion],voy_direccion));
                        }else{
                            if(direcciones_ayuda[voy_direccion_ayuda]==3){
                                status = down();
                                log(String.format("MOVERSEEE EN: %d estoy en %d ",direcciones_ayuda[voy_direccion],voy_direccion));
                            }else{
                                if(direcciones_ayuda[voy_direccion_ayuda]==4){
                                    status = sense();
                                }
                            }
                        }
                    }
                }



                log(String.format("3. DATOS DIRECCION AYUDA: %d %d %d %d %d %d YYY %d",direcciones_ayuda[0],direcciones_ayuda[1],direcciones_ayuda[2]
                        ,direcciones_ayuda[3],direcciones_ayuda[4],direcciones_ayuda[4],voy_direccion_ayuda));


                log(String.format("1. ENTROOOOO AYUDA: %d da %d ",voy_direccion_ayuda,direcciones_ayuda[voy_direccion_ayuda]));

                if(direcciones_ayuda[voy_direccion_ayuda+1]==5) {
                    posagentesnvo[getAgentId()-1][2] = direcciones_ayuda[voy_direccion_ayuda];
                    log(String.format("2. ENTROOOOO: %d da %d ",voy_direccion_ayuda,direcciones_ayuda[voy_direccion_ayuda]));
                    voy_direccion_ayuda=0;

                    Arrays.fill(direcciones_ayuda, 5);
                }

                voy_direccion_ayuda++;


            }













            //CUANDO SE TRANCA
            if(posagentesnvo[getAgentId()-1][0]!=status.agentX || posagentesnvo[getAgentId()-1][1]!=status.agentY) {
                posagentesant[getAgentId()-1][0]=posagentesnvo[getAgentId()-1][0];
                posagentesant[getAgentId()-1][1]=posagentesnvo[getAgentId()-1][1];
                posagentesnvo[getAgentId()-1][0]=status.agentX; posagentesnvo[getAgentId()-1][1]=status.agentY;
            }else {
                if(esperando_recoger_oro==0) {

                    if (((posagentesant[getAgentId() - 1][0] - posagentesnvo[getAgentId() - 1][0]) > 0)) { //LLEGA A LA IZQ
                        log(String.format("Eentrooo izqqqqqqq "));
                        posagentesnvo[getAgentId() - 1][2] = 4;
                        if (status.agentX == 0) {
                            log(String.format("STUCK  BORDE "));
                            if (status.agentY == 0) {
                                log(String.format("1. STUCK  BORDE "));
                                voy_direccion = 0;
                                direcciones_tranca[0] = 3; //baja
                                direcciones_tranca[1] = 3; //baja
                                direcciones_tranca[2] = 3; //baja
                                direcciones_tranca[3] = 1; //der
                                direcciones_tranca[4] = 5; //der
                                if(estoy_ayudando==1){
                                    voy_direccion_ayuda = 0;
                                    direcciones_ayuda[0] = 3; //baja
                                    direcciones_ayuda[1] = 3; //baja
                                    direcciones_ayuda[2] = 3; //baja
                                    direcciones_ayuda[3] = 1; //der
                                    direcciones_ayuda[4] = 5; //der
                                }
                            } else {
                                if (status.agentY == status.height - 1) {
                                    log(String.format("2. STUCK  BORDE "));
                                    voy_direccion = 0;
                                    direcciones_tranca[0] = 2; //sube
                                    direcciones_tranca[1] = 2; //sube
                                    direcciones_tranca[2] = 2; //sube
                                    direcciones_tranca[3] = 1; //der
                                    direcciones_tranca[4] = 5; //der
                                    if(estoy_ayudando==1){
                                        voy_direccion_ayuda = 0;
                                        direcciones_ayuda[0] = 2; //sube
                                        direcciones_ayuda[1] = 2; //sube
                                        direcciones_ayuda[2] = 2; //sube
                                        direcciones_ayuda[3] = 1; //der
                                        direcciones_ayuda[4] = 5; //der
                                    }
                                } else {

                                    log(String.format("5. STUCK  BORDE "));
                                    int lleno_arriba = 0;
                                    int lleno_abajo = 0;
                                    int entro_arriba =0;
                                    int entro_abajo =0;

                                    if (status.agentY + 2 < status.height) {
                                        entro_abajo = 1;
                                        for (int i = 0; i < status.width; i++) {
                                            log(String.format("1.1 MATRIZ GUARDADA %d,%d res: %d,  %d,%d res: %d",
                                                    i,status.agentY, matriz_ubicaciones[status.agentY][i]
                                                    , i, status.agentY + 2, matriz_ubicaciones[status.agentY + 2][i]));
                                            if (matriz_ubicaciones[status.agentY + 2][i] != 0) {
                                                lleno_abajo++;
                                            }
                                        }
                                    } else {
                                        if (status.agentY + 1 < status.height) {
                                            entro_abajo = 1;
                                            for (int i = 0; i < status.width; i++) {
                                                log(String.format("1.2 MATRIZ GUARDADA %d,%d res: %d,  %d,%d res: %d",
                                                        i, status.agentY, matriz_ubicaciones[status.agentY][i],
                                                        i,  status.agentY + 1, matriz_ubicaciones[status.agentY + 1][i]));
                                                if (matriz_ubicaciones[status.agentY + 1][i] != 0) {
                                                    lleno_abajo++;
                                                }
                                            }
                                        }
                                    }
                                    if (status.agentY - 2 > 0) {
                                        entro_arriba = 1;
                                        for (int i = 0; i < status.width; i++) {
                                            log(String.format("1.3 MATRIZ GUARDADA %d,%d res: %d,  %d,%d res: %d",
                                                    i,status.agentY,  matriz_ubicaciones[status.agentY][i]
                                                    , i, status.agentY - 2, matriz_ubicaciones[status.agentY - 2][i]));
                                            if (matriz_ubicaciones[status.agentY - 2][i] != 0) {
                                                lleno_arriba++;
                                            }
                                        }
                                    } else {
                                        if (status.agentY - 1 > 0) {
                                            entro_arriba = 1;
                                            for (int i = 0; i < status.width; i++) {
                                                log(String.format("1.4 MATRIZ GUARDADA %d,%d res: %d,  %d,%d res: %d",
                                                        status.agentY, i, matriz_ubicaciones[status.agentY][i]
                                                        , status.agentY - 1, i, matriz_ubicaciones[status.agentY - 1][i]));
                                                if (matriz_ubicaciones[status.agentY - 1][i] != 0) {
                                                    lleno_arriba++;
                                                }
                                            }
                                        }
                                    }


                                    log(String.format("1. INTERNO ANTES DE TOTALES  %d, %d, recorrio: %d, %d",
                                            lleno_arriba, lleno_abajo,entro_arriba,entro_abajo));

                                    if(lleno_abajo<lleno_arriba && entro_abajo==1) {
                                        if (lleno_abajo < status.height) {
                                            voy_direccion = 0;
                                            direcciones_tranca[0] = 3; //baja
                                            direcciones_tranca[1] = 3; //baja
                                            direcciones_tranca[2] = 3; //baja
                                            direcciones_tranca[3] = 1; //izq
                                            direcciones_tranca[4] = 5; //der
                                            log(String.format("7.1 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_abajo));
                                            if(estoy_ayudando==1){
                                                voy_direccion_ayuda = 0;
                                                direcciones_ayuda[0] = 3; //baja
                                                direcciones_ayuda[1] = 3; //baja
                                                direcciones_ayuda[2] = 3; //baja
                                                direcciones_ayuda[3] = 1; //izq
                                                direcciones_ayuda[4] = 5; //der

                                            }

                                        } else {
                                            if (lleno_arriba < status.height) {
                                                voy_direccion = 0;
                                                direcciones_tranca[0] = 2; //sube
                                                direcciones_tranca[1] = 2; //sube
                                                direcciones_tranca[2] = 2; //sube
                                                direcciones_tranca[3] = 1; //izq
                                                direcciones_tranca[4] = 5; //der
                                                log(String.format("7.2 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_abajo));
                                                if(estoy_ayudando==1){
                                                    voy_direccion_ayuda = 0;

                                                    direcciones_ayuda[0] = 2; //sube
                                                    direcciones_ayuda[1] = 2; //sube
                                                    direcciones_ayuda[2] = 2; //sube
                                                    direcciones_ayuda[3] = 1; //izq
                                                    direcciones_ayuda[4] = 5; //der


                                                }

                                            }
                                        }
                                    }else{
                                        if (lleno_arriba < status.height && entro_arriba==1) {
                                            voy_direccion = 0;
                                            direcciones_tranca[0] = 2; //sube
                                            direcciones_tranca[1] = 2; //sube
                                            direcciones_tranca[2] = 2; //sube
                                            direcciones_tranca[3] = 1; //izq
                                            direcciones_tranca[4] = 5; //der
                                            log(String.format("7.3 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_arriba));
                                            if(estoy_ayudando==1){
                                                voy_direccion_ayuda = 0;

                                                direcciones_ayuda[0] = 2; //sube
                                                direcciones_ayuda[1] = 2; //sube
                                                direcciones_ayuda[2] = 2; //sube
                                                direcciones_ayuda[3] = 1; //izq
                                                direcciones_ayuda[4] = 5; //der


                                            }


                                        } else {
                                            if (lleno_abajo <= status.height) {
                                                voy_direccion = 0;
                                                direcciones_tranca[0] = 3; //baja
                                                direcciones_tranca[1] = 3; //baja
                                                direcciones_tranca[2] = 3; //baja
                                                direcciones_tranca[3] = 1; //izq
                                                direcciones_tranca[4] = 5; //der
                                                log(String.format("7.4 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_arriba));
                                                if(estoy_ayudando==1){
                                                    voy_direccion_ayuda = 0;

                                                    direcciones_ayuda[0] = 3; //baja
                                                    direcciones_ayuda[1] = 3; //baja
                                                    direcciones_ayuda[2] = 3; //baja
                                                    direcciones_ayuda[3] = 1; //izq
                                                    direcciones_ayuda[4] = 5; //der

                                                }

                                            }else{
                                                voy_direccion = 0;
                                                direcciones_tranca[0] = 2; //sube
                                                direcciones_tranca[1] = 2; //sube
                                                direcciones_tranca[2] = 2; //sube
                                                direcciones_tranca[3] = 1; //izq
                                                direcciones_tranca[4] = 5; //der
                                                log(String.format("7.5 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_arriba));

                                                if(estoy_ayudando==1){
                                                    voy_direccion_ayuda = 0;

                                                    direcciones_ayuda[0] = 2; //sube
                                                    direcciones_ayuda[1] = 2; //sube
                                                    direcciones_ayuda[2] = 2; //sube
                                                    direcciones_ayuda[3] = 1; //izq
                                                    direcciones_ayuda[4] = 5; //der


                                                }

                                            }
                                        }

                                    }
                                    log(String.format("1. TOTALEES %d, %d", lleno_arriba, lleno_abajo));


                                }
                            }
                        } else {
                            log(String.format("1. STUCK  pero no en borde derecha primera"));
/*

                            if(status.agentY-1>=0 && status.agentY+1<status.height){
                                log(String.format("1.1 STUCK  pero no en borde izquierda   alrededor: %d, %d: %d  y %d, %d: %d",
                                        status.agentY-1, status.agentX-1, matriz_ubicaciones[status.agentY-1][status.agentX-1]
                                        ,status.agentY-1, status.agentX+1, matriz_ubicaciones[status.agentY-1][status.agentX+1]));
                            }else{
                                if(status.agentY-1>=0){
                                    log(String.format("1.2 STUCK  pero no en borde izquierda   alrededor:  %d, %d: %d",
                                            status.agentY-1, status.agentX-1, matriz_ubicaciones[status.agentY-1][status.agentX-1]
                                            ));
                                }else{
                                    if(status.agentY+1<status.height){
                                        log(String.format("1.3 STUCK  pero no en borde izquierda   alrededor:%d, %d: %d",
                                                status.agentY-1, status.agentX+1, matriz_ubicaciones[status.agentY-1][status.agentX+1]));
                                    }else{
                                        log(String.format("1.4 STUCK  pero no en borde izquierda   alrededor NO SE PUEDE"));

                                    }
                                }
                            }
                            */


                            if (status.agentY > 0) {
                                log(String.format("1. STUCK  pero no en borde izquierda primero"));
                                voy_direccion = 0;
                                direcciones_tranca[0] = 2;
                                direcciones_tranca[1] = 0;
                                direcciones_tranca[2] = 0;
                                direcciones_tranca[3] = 3;
                                direcciones_tranca[4] = 0;
                                if(estoy_ayudando==1){
                                    voy_direccion_ayuda = 0;
                                    direcciones_ayuda[0] = 2;
                                    direcciones_ayuda[1] = 0;
                                    direcciones_ayuda[2] = 0;
                                    direcciones_ayuda[3] = 3;
                                    direcciones_ayuda[4] = 0;


                                }
                            } else {
                                log(String.format("1. STUCK  pero no en borde izquierda segundo"));
                                voy_direccion = 0;
                                direcciones_tranca[0] = 3;
                                direcciones_tranca[1] = 0;
                                direcciones_tranca[2] = 0;
                                direcciones_tranca[3] = 2;
                                direcciones_tranca[4] = 0;
                                if(estoy_ayudando==1){
                                    voy_direccion_ayuda = 0;
                                    direcciones_ayuda[0] = 3;
                                    direcciones_ayuda[1] = 0;
                                    direcciones_ayuda[2] = 0;
                                    direcciones_ayuda[3] = 2;
                                    direcciones_ayuda[4] = 0;
                                }
                            }

                        }

                        log(String.format("STUCK  izquierda "));
                    } else {
                        if (((posagentesant[getAgentId() - 1][0] - posagentesnvo[getAgentId() - 1][0]) < 0)) { //LLEGA A LA DERECHA

                            log(String.format("Eentrooo deeeeer "));
                            posagentesnvo[getAgentId() - 1][2] = 4;
                            if (status.agentX == status.width - 1) {
                                log(String.format("STUCK  BORDE "));
                                if (status.agentY == 0) {
                                    log(String.format("3. STUCK  BORDE EEEEEEEEEEEEEEEENTRAAAAAAAAAAAAAAAA "));
                                    voy_direccion = 0;
                                    direcciones_tranca[0] = 3; //baja
                                    direcciones_tranca[1] = 3; //baja
                                    direcciones_tranca[2] = 3; //baja
                                    direcciones_tranca[3] = 0; //der
                                    direcciones_tranca[4] = 5; //der
                                    if(estoy_ayudando==1){
                                        voy_direccion_ayuda = 0;
                                        direcciones_ayuda[0] = 3; //baja
                                        direcciones_ayuda[1] = 3; //baja
                                        direcciones_ayuda[2] = 3; //baja
                                        direcciones_ayuda[3] = 0; //der
                                        direcciones_ayuda[4] = 5; //der
                                    }

                                } else {
                                    if (status.agentY == status.height - 1) {
                                        log(String.format("4. STUCK  BORDE "));
                                        voy_direccion = 0;
                                        direcciones_tranca[0] = 2; //sube
                                        direcciones_tranca[1] = 2; //sube
                                        direcciones_tranca[2] = 2; //sube
                                        direcciones_tranca[3] = 0; //der
                                        direcciones_tranca[4] = 5; //der
                                        if(estoy_ayudando==1){
                                            voy_direccion_ayuda = 0;
                                            direcciones_ayuda[0] = 2; //sube
                                            direcciones_ayuda[1] = 2; //sube
                                            direcciones_ayuda[2] = 2; //sube
                                            direcciones_ayuda[3] = 0; //der
                                            direcciones_ayuda[4] = 5; //der
                                        }
                                    } else {
                                        log(String.format("6. STUCK  BORDE "));
                                        int lleno_arriba = 0;
                                        int lleno_abajo = 0;
                                        int entro_arriba =0;
                                        int entro_abajo =0;


                                        if (status.agentY + 2 < status.height) {
                                            entro_abajo=1;
                                            for (int i = 0; i < status.width; i++) {
                                                log(String.format("2.1 MATRIZ GUARDADA %d,%d res: %d,  %d,%d res: %d",
                                                        i,status.agentY, matriz_ubicaciones[status.agentY][i]
                                                        , i, status.agentY + 2, matriz_ubicaciones[status.agentY + 2][i]));
                                                if (matriz_ubicaciones[status.agentY + 2][i] != 0) {
                                                    lleno_abajo++;
                                                }
                                            }
                                        } else {
                                            if (status.agentY + 1 < status.height) {
                                                entro_abajo=1;
                                                for (int i = 0; i < status.width; i++) {
                                                    log(String.format("2.2 MATRIZ GUARDADA %d,%d res: %d,  %d,%d res: %d",
                                                            i, status.agentY, matriz_ubicaciones[status.agentY][i],
                                                            i,  status.agentY + 1, matriz_ubicaciones[status.agentY + 1][i]));
                                                    if (matriz_ubicaciones[status.agentY + 1][i] != 0) {
                                                        lleno_abajo++;
                                                    }
                                                }
                                            }
                                        }
                                        if (status.agentY - 2 > 0) {
                                            entro_arriba=1;
                                            for (int i = 0; i < status.width; i++) {
                                                log(String.format("2.3 MATRIZ GUARDADA %d,%d res: %d,  %d,%d res: %d",
                                                        i,status.agentY,  matriz_ubicaciones[status.agentY][i]
                                                        , i, status.agentY - 2, matriz_ubicaciones[status.agentY - 2][i]));
                                                if (matriz_ubicaciones[status.agentY - 2][i] != 0) {
                                                    lleno_arriba++;
                                                }
                                            }
                                        } else {
                                            if (status.agentY - 1 > 0) {
                                                entro_arriba=1;
                                                for (int i = 0; i < status.width; i++) {
                                                    log(String.format("2.4 MATRIZ GUARDADA %d,%d res: %d,  %d,%d res: %d",
                                                            status.agentY, i, matriz_ubicaciones[status.agentY][i]
                                                            , status.agentY - 1, i, matriz_ubicaciones[status.agentY - 1][i]));
                                                    if (matriz_ubicaciones[status.agentY - 1][i] != 0) {
                                                        lleno_arriba++;
                                                    }
                                                }
                                            }
                                        }



                                        log(String.format("2. INTERNO ANTES DE TOTALES  %d, %d, recorrio: %d, %d",
                                                lleno_arriba, lleno_abajo,entro_arriba,entro_abajo));
                                        if(lleno_abajo<lleno_arriba && entro_abajo==1) {
                                            if (lleno_abajo < status.height) {
                                                voy_direccion = 0;
                                                direcciones_tranca[0] = 3; //baja
                                                direcciones_tranca[1] = 3; //baja
                                                direcciones_tranca[2] = 3; //baja
                                                direcciones_tranca[3] = 0; //izq
                                                direcciones_tranca[4] = 5; //der
                                                log(String.format("8.1 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_abajo));
                                                if(estoy_ayudando==1){
                                                    voy_direccion_ayuda = 0;
                                                    direcciones_ayuda[0] = 3; //baja
                                                    direcciones_ayuda[1] = 3; //baja
                                                    direcciones_ayuda[2] = 3; //baja
                                                    direcciones_ayuda[3] = 0; //izq
                                                    direcciones_ayuda[4] = 5; //der
                                                }

                                            } else {
                                                if (lleno_arriba < status.height) {
                                                    voy_direccion = 0;
                                                    direcciones_tranca[0] = 2; //sube
                                                    direcciones_tranca[1] = 2; //sube
                                                    direcciones_tranca[2] = 2; //sube
                                                    direcciones_tranca[3] = 0; //izq
                                                    direcciones_tranca[4] = 5; //der
                                                    log(String.format("8.2 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_abajo));
                                                    if(estoy_ayudando==1){
                                                        voy_direccion_ayuda = 0;
                                                        direcciones_ayuda[0] = 2; //sube
                                                        direcciones_ayuda[1] = 2; //sube
                                                        direcciones_ayuda[2] = 2; //sube
                                                        direcciones_ayuda[3] = 0; //izq
                                                        direcciones_ayuda[4] = 5; //der
                                                    }

                                                }
                                            }
                                        }else{
                                            if (lleno_arriba < status.height && entro_arriba==1) {
                                                voy_direccion = 0;
                                                direcciones_tranca[0] = 2; //sube
                                                direcciones_tranca[1] = 2; //sube
                                                direcciones_tranca[2] = 2; //sube
                                                direcciones_tranca[3] = 0; //izq
                                                direcciones_tranca[4] = 5; //der
                                                log(String.format("8.3 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_arriba));

                                                if(estoy_ayudando==1){
                                                    voy_direccion_ayuda = 0;
                                                    direcciones_ayuda[0] = 2; //sube
                                                    direcciones_ayuda[1] = 2; //sube
                                                    direcciones_ayuda[2] = 2; //sube
                                                    direcciones_ayuda[3] = 0; //izq
                                                    direcciones_ayuda[4] = 5; //der
                                                }



                                            } else {
                                                if (lleno_abajo <= status.height) {
                                                    voy_direccion = 0;
                                                    direcciones_tranca[0] = 3; //baja
                                                    direcciones_tranca[1] = 3; //baja
                                                    direcciones_tranca[2] = 3; //baja
                                                    direcciones_tranca[3] = 0; //izq
                                                    direcciones_tranca[4] = 5; //der
                                                    log(String.format("8.4 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_arriba));

                                                    if(estoy_ayudando==1){
                                                        voy_direccion_ayuda = 0;
                                                        direcciones_ayuda[0] = 3; //baja
                                                        direcciones_ayuda[1] = 3; //baja
                                                        direcciones_ayuda[2] = 3; //baja
                                                        direcciones_ayuda[3] = 0; //izq
                                                        direcciones_ayuda[4] = 5; //der
                                                    }

                                                }else{
                                                    voy_direccion = 0;
                                                    direcciones_tranca[0] = 2; //sube
                                                    direcciones_tranca[1] = 2; //sube
                                                    direcciones_tranca[2] = 2; //sube
                                                    direcciones_tranca[3] = 0; //izq
                                                    direcciones_tranca[4] = 5; //der
                                                    log(String.format("8.5 ENTRO BORDE  %d, %d, recorrio: %d", lleno_arriba, lleno_abajo,entro_arriba));

                                                    if(estoy_ayudando==1){
                                                        voy_direccion_ayuda = 0;
                                                        direcciones_ayuda[0] = 2; //sube
                                                        direcciones_ayuda[1] = 2; //sube
                                                        direcciones_ayuda[2] = 2; //sube
                                                        direcciones_ayuda[3] = 0; //izq
                                                        direcciones_ayuda[4] = 5; //der
                                                    }

                                                }
                                            }

                                        }

                                        log(String.format("2. TOTALEES %d, %d", lleno_arriba, lleno_abajo));


                                    }
                                }
                            } else {

                                if (status.agentY > 0) {
                                    log(String.format("2. STUCK  pero no en borde derecha primera"));
                                    /*
                                    if(status.agentY+1<status.height && status.agentY-1>=0){
                                        log(String.format("2.1 STUCK  pero no en borde derecha   alrededor: %d, %d: %d  y %d, %d: %d",
                                                status.agentY+1, status.agentX-1, matriz_ubicaciones[status.agentY+1][status.agentX-1]
                                                ,status.agentY+1, status.agentX+1, matriz_ubicaciones[status.agentY+1][status.agentX+1]));

                                    }else{
                                        if(status.agentY+1<status.height){
                                            log(String.format("2.2 STUCK  pero no en borde derecha   alrededor:  %d, %d: %d"
                                                    ,status.agentY+1, status.agentX+1, matriz_ubicaciones[status.agentY+1][status.agentX+1]));

                                        } else{
                                            if( status.agentY-1>=0){
                                                log(String.format("2.3 STUCK  pero no en borde derecha   alrededor: %d, %d: %d",
                                                        status.agentY+1, status.agentX-1, matriz_ubicaciones[status.agentY+1][status.agentX-1]));

                                            }else{

                                                log(String.format("2.4 STUCK  pero no en borde derecha   alrededor NO SE PUEDE"));
                                            }
                                        }
                                    }
                                    */


                                    voy_direccion = 0;
                                    direcciones_tranca[0] = 2;
                                    direcciones_tranca[1] = 1;
                                    direcciones_tranca[2] = 1;
                                    direcciones_tranca[3] = 3;
                                    direcciones_tranca[4] = 1;
                                    direcciones_tranca[5] = 5;

                                    if(estoy_ayudando==1){
                                        voy_direccion_ayuda = 0;
                                        direcciones_ayuda[0] = 2;
                                        direcciones_ayuda[1] = 1;
                                        direcciones_ayuda[2] = 1;
                                        direcciones_ayuda[3] = 3;
                                        direcciones_ayuda[4] = 1;
                                        direcciones_ayuda[5] = 5;
                                    }
                                } else {
                                    log(String.format("2. STUCK  pero no en borde derecha segunda"));
                                    if(status.agentY+1<status.height && status.agentY-1>=0){
                                        log(String.format("2.5 STUCK  pero no en borde derecha   alrededor: %d, %d: %d  y %d, %d: %d",
                                                status.agentY+1, status.agentX-1, matriz_ubicaciones[status.agentY+1][status.agentX-1]
                                                ,status.agentY+1, status.agentX+1, matriz_ubicaciones[status.agentY+1][status.agentX+1]));

                                    }else{
                                        if(status.agentY+1<status.height){
                                            log(String.format("2.6 STUCK  pero no en borde derecha   alrededor:  %d, %d: %d"
                                                    ,status.agentY+1, status.agentX+1, matriz_ubicaciones[status.agentY+1][status.agentX+1]));

                                        } else{
                                            if( status.agentY-1>=0){
                                                log(String.format("2.7 STUCK  pero no en borde derecha   alrededor: %d, %d: %d",
                                                        status.agentY+1, status.agentX-1, matriz_ubicaciones[status.agentY+1][status.agentX-1]));

                                            }else{

                                                log(String.format("2.8 STUCK  pero no en borde derecha   alrededor NO SE PUEDE"));
                                            }
                                        }
                                    }


                                    voy_direccion = 0;
                                    direcciones_tranca[0] = 3;
                                    direcciones_tranca[1] = 1;
                                    direcciones_tranca[2] = 1;
                                    direcciones_tranca[3] = 2;
                                    direcciones_tranca[4] = 1;
                                    direcciones_tranca[5] = 5;

                                    if(estoy_ayudando==1){
                                        voy_direccion_ayuda = 0;
                                        direcciones_ayuda[0] = 3;
                                        direcciones_ayuda[1] = 1;
                                        direcciones_ayuda[2] = 1;
                                        direcciones_ayuda[3] = 2;
                                        direcciones_ayuda[4] = 1;
                                        direcciones_ayuda[5] = 5;
                                    }
                                }

                            }
                            log(String.format("STUCK  derecha "));


                        }else{

                            if(((posagentesant[getAgentId() - 1][1] - posagentesnvo[getAgentId() - 1][1]) < 0)){//LLEGA A ABAJO
                                log(String.format("STUCK  ABAJO "));

                            }else{
                                if(((posagentesant[getAgentId() - 1][1] - posagentesnvo[getAgentId() - 1][1]) > 0)){ //LLEGA A ARRIBA
                                    log(String.format("STUCK  ARRIBA "));

                                }
                            }
                        }
                    }
                }else{




                }

                log(String.format("STUCK  [%d,%d] Y data nueva   [%d,%d] ", posagentesant[getAgentId()-1][0], posagentesant[getAgentId()-1][1]
                        ,  posagentesnvo[getAgentId()-1][0],posagentesnvo[getAgentId()-1][1] ));
            }




            if(getAgentId()==1) {
                int volver_empezar = 0;
                for (int[] row : matriz_ubicaciones) {
                    //for each number in the row
                    for (int j : row) {
                        System.out.print(j + " ");
                        if(j==0){
                            volver_empezar=1;
                        }
                    }
                    System.out.println("");
                }

                if(volver_empezar==0){
                    for(int i=0; i<status.width;i++){
                        for(int j=0; j<status.height; j++){
                            matriz_ubicaciones[i][j]=0;
                        }
                    }
                    log(String.format("Volver a empezar " ));
                }


            }







            try {
                Thread.sleep(200);
            } catch(InterruptedException ie) {}
        }
    }
}
