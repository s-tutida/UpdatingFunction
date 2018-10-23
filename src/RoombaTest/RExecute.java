package RoombaTest;


import java.io.IOException;

import Tsuchida.ControlLoop;
import Tsuchida.Execute;
import EventConverter.KnowledgeState;
import gnu.io.SerialPort;

public class RExecute extends Execute{

    SerialCommunication sc = null;
    SerialPort serialPort = null;
    ControlLoop cm = null;
    Integer check = 0;
	
	public RExecute(ControlLoop in_cm, String name, SerialCommunication in_sc) {
		super(in_cm, name);
		this.cm = in_cm;
		this.sc = in_sc;
	}
	
	@Override
	public Object execute(Object o) {
		String str = String.valueOf(o);
		String[] commands = str.split(",", 0);
		
		if(check == 1) {//既に到達した場合.
			return null;
		}
        
		if(sc!=null){//通信がある.
			for(String command: commands){
				if(! (command==null || command.isEmpty())) {//実行コマンドがある
										
					//MAPEの終了
					if(Integer.parseInt(command)==5) {//start to clean
						check = 1;
						System.out.println("      Arrive at target point");
						System.out.println("      Send arriveSpot event to EventConverter.");
						sc.send_command(2);//Reset
						try {
							this.cm.exit();
							((KnowledgeState) this.cm.getKnowledge()).setEvent("arriveSpot");
						}catch(Exception e){
						}
						return null;
					}
					
					sc.send_command(Integer.parseInt(command));
					
					//実行後, 少し時間をとる. MAPEと次のMAPEの間の時間を調整.
					try {
						Thread.sleep(700);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
