package RoombaTest;


import java.io.IOException;

import Tsuchida.ControlLoop;
import Tsuchida.Execute;
import gnu.io.SerialPort;

public class RExecute extends Execute{

    SerialCommunication sc = null;
    SerialPort serialPort = null;
    ControlLoop cm = null;
    Integer check = 0;
	
	public RExecute(ControlLoop cm, String name, SerialCommunication in_sc) {
		super(cm, name);
		sc = in_sc;
	}
	
	@Override
	public Object execute(Object o) {
		String str = String.valueOf(o);
		String[] commands = str.split(",", 0);
		
		if(check == 1) {
			return null;
		}
        
		if(sc!=null){//通信がある.
			for(String command: commands){
				if(! (command==null || command.isEmpty())) {//実行コマンドがある
					sc.send_command(Integer.parseInt(command));
					
					//MAPEの終了
					if(Integer.parseInt(command)==5) {
						check = 1;
						((ControlLoop) super.cm).exit();
					}
					
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
