package EventConverter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {
	
	private Element rootElement = null;//inputしたxmlのドキュメントオブジェクト
	
	// 状態のリスト 例:[[状態], [番号],]
	private Map<String, Integer> original_state_list = new HashMap();
	private Map<String, Integer> new_state_list = new HashMap();
	
	// reference id, event_name のセット
	private Map<String, String> original_event_refid_list = new HashMap();
	private Map<String, String> new_event_refid_list = new HashMap();
	
	// 遷移のリスト 例:["event_refid"][[遷移元番号],[遷移先番号]]
	private Map<String, Integer[]> original_event_list = new HashMap<String, Integer[]>();
	private Map<String, Integer[]> new_event_list = new HashMap<String, Integer[]>();
	
	public Parser() throws SAXException, IOException, ParserConfigurationException {
		
		// -----  prepare document instances  ------
		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new File("cleaningRobot.xml"));
		this.rootElement =document.getDocumentElement();
		
		// ----- parse -----
		parse();

	}

	// parser XML
	public void parse() throws SAXException, IOException, ParserConfigurationException {
		
		//ID of an original and a new state mahcine diagrams
		Map<String, String> state_list = getDiagramInfo();
		Integer original_id = getOriginalID(state_list);
		Integer new_id = getNewID(state_list);
		
		//state list (state_name, state_id)
		this.original_state_list = makeStateList(original_id);
		this.new_state_list = makeStateList(new_id);		
		
		//event reference list [event_reference_id, event_name]
		this.original_event_refid_list = makeEventInfo(original_id);
		this.new_event_refid_list = makeEventInfo(new_id);

		//event list ["event_reference_id", ("from", "to")]
		this.original_event_list = makeEventInfo(original_state_list, original_event_refid_list, original_id);
		this.new_event_list = makeEventInfo(new_state_list, new_event_refid_list, new_id);
	
	}
		
	//orignial, new それぞれのnameとrefidのセットをMapで返す関数 / なければnull
	private Map<String, String> getDiagramInfo() {
		
		Map<String, String> state_list = new HashMap();
		
		//astahプロジェクト内のoriginal, newのstate_machineの(name, refid)を獲得する.
		NodeList state_machine_diagrams = this.rootElement.getElementsByTagName("JUDE:StateChartDiagram");
		int i = 0;
		while(state_machine_diagrams.item(i)!=null) {
			
			Element diagram_old = (Element) state_machine_diagrams.item(i);
			String state_name = diagram_old.getAttribute("name");
			
			NodeList refs_old = diagram_old.getElementsByTagName("JUDE:StateChartDiagram.semanticModel");	
			Node ref_old = refs_old.item(0);
			Node ref_old_tmp = ref_old.getFirstChild();
			Node next_old = ref_old_tmp.getNextSibling();

			String state_refid = ((Element) next_old).getAttribute("xmi.idref");
			
			if(state_name.equals("new")||state_name.equals("original")) {
				state_list.put(state_name, state_refid);		
			}
			i++;
		}
		
		//original, newの2つがあるかどうか確認する.
		if(state_list.containsKey("new")||state_list.containsKey("original")) {
			return state_list;
		}
		
		return null;
	}
	
	//get OriginalID to search and use original state machine.
	private Integer getOriginalID(Map<String, String> map) {
		
		NodeList ownedElement = this.rootElement.getElementsByTagName("UML:Namespace.ownedElement");
		NodeList diagrams = ownedElement.item(0).getChildNodes();
	
		//state
		int i = 1;
		while(diagrams.item(i)!=null) {
			
			if(map.get("original").equals(((Element) diagrams.item(i)).getAttribute("xmi.id"))) {
				return i;
			}
			i = i + 2;
		}
		
		return 0;
	}
	
	//get NewID to search and use new state machine.
	private Integer getNewID(Map<String, String> map) {
		
		NodeList ownedElement = this.rootElement.getElementsByTagName("UML:Namespace.ownedElement");
		NodeList diagrams = ownedElement.item(0).getChildNodes();
	
		//state
		int i = 1;
		while(diagrams.item(i)!=null) {
			if(map.get("new").equals(((Element) diagrams.item(i)).getAttribute("xmi.id"))) {
				return i;
			}
			i = i + 2;
		}
		
		return 0;
	}
	
	//getStateList  (Clean => 0 , Off => 1 , on => 2, clean => 3, spot => 4)
	private Map<String, Integer> makeStateList(Integer i){
		
		Map<String, Integer> state_list = new HashMap();
		
		NodeList ownedElement = this.rootElement.getElementsByTagName("UML:Namespace.ownedElement");
		NodeList diagrams = ownedElement.item(0).getChildNodes();
	
		//state
		Element old_tmp = (Element) diagrams.item(i);
		NodeList old_tmp_2 = old_tmp.getElementsByTagName("UML:CompositeState.subvertex");//状態が格納されている一層上まで, 移動.
		Element old_tmp_3 = (Element)  old_tmp_2.item(0); //状態(state)が格納されている層まで, 移動.

		Node m = old_tmp_3.getFirstChild();
		
		int k = 0;
		while(m != null) {
			if(m.getNodeType()==Node.ELEMENT_NODE) {
				//state name
				state_list.put(((Element) m ).getAttribute("name"), k);
				k++;
			}
			m = m.getNextSibling();
		}
		
		return state_list;
	}
	
	//getEventInfo (["event_reference_id"]["event_name"])
	private Map<String, String> makeEventInfo(Integer i) {
			
			Map<String, String> event_list = new HashMap();
			
			NodeList ownedElement = this.rootElement.getElementsByTagName("UML:Namespace.ownedElement");
			NodeList diagrams = ownedElement.item(0).getChildNodes();
			Element old_tmp = (Element) diagrams.item(i);
			NodeList old_tmp_4 = old_tmp.getElementsByTagName("UML:StateMachine.transitions");
			Element old_tmp_5 = (Element)  old_tmp_4.item(0); 
		
			Node m = old_tmp_5.getFirstChild();
		
			while(m != null) {
				if(m.getNodeType()==Node.ELEMENT_NODE) {
					//state name
					String name = ((Element) m ).getAttribute("name");

					if(name == null || name.isEmpty()) {
						name = "Initial_event";
					}
					event_list.put(((Element) m ).getAttribute("xmi.id"), name);//event_refid, name
				}
				m = m.getNextSibling();
			}
			
			return event_list;
			
		}
		
	//getEventInfo (["event_reference_id"][[state_number_from],[state_number_to]])
	private Map<String, Integer[]> makeEventInfo(Map<String, Integer> state_list, Map<String, String> event_refid_list, Integer i) {
		
		//This is answer
		Map<String, Integer[]> event_list = new HashMap<String, Integer[]>();
		
		// event_listのIDを埋める
		for (String refid : event_refid_list.keySet()) {
			Integer[] m = {-1, -1};
			event_list.put(refid, m);
		}
		
		//以下, マージ作業
		NodeList ownedElement = this.rootElement.getElementsByTagName("UML:Namespace.ownedElement");
		NodeList diagrams = ownedElement.item(0).getChildNodes();
	
		//state
		Element old_tmp = (Element) diagrams.item(i);
		NodeList old_tmp_2 = old_tmp.getElementsByTagName("UML:CompositeState.subvertex");//状態が格納されている一層上まで, 移動.
		Element old_tmp_3 = (Element)  old_tmp_2.item(0); //状態(state)が格納されている層まで, 移動.
		Node m = old_tmp_3.getFirstChild();
		
		while(m != null) {
			if(m.getNodeType()==Node.ELEMENT_NODE) {
			
				//state name
				Integer state_number = state_list.get(((Element) m ).getAttribute("name"));
				
				//outgoing event
				NodeList outgoing_list = ((Element) m ).getElementsByTagName("UML:StateVertex.outgoing");
				Node outgoing = outgoing_list.item(0).getFirstChild();
				while(outgoing!=null) {
					if(outgoing.getNodeType()==Node.ELEMENT_NODE) {
						String refid = ((Element)outgoing).getAttribute("xmi.idref");
						Integer[] m1 = event_list.get(refid);
						m1[0] = state_number;
						event_list.put(refid, m1);
					}
					outgoing = outgoing.getNextSibling();
				}
				

				
				//incoming event
				NodeList incoming_list = ((Element)m).getElementsByTagName("UML:StateVertex.incoming");
				if(incoming_list != null && incoming_list.item(0)!=null) {
					if(incoming_list.item(0).hasChildNodes()) {
						Node incoming =  incoming_list.item(0).getFirstChild();
						while(incoming!=null) {
							if(incoming.getNodeType()==Node.ELEMENT_NODE) {
								String refid = ((Element)incoming).getAttribute("xmi.idref");
								Integer[] m1 = event_list.get(refid);
								m1[1] = state_number;
								event_list.put(refid, m1);
							}
							incoming = incoming.getNextSibling();
						}
					}
				}
			}
			m = m.getNextSibling();
		}
		
		return event_list;
		
	}
	
	
	public Map<String, Integer> get_original_state_list(){
		return this.original_state_list;
	}
	
	public Map<String, Integer> get_new_state_list(){
		return this.new_state_list;
	}
	
	public Map<String, String> get_original_event_refid_list(){
		return this.original_event_refid_list;
	}
	
	public Map<String, String> get_new_event_refid_list(){
		return this.new_event_refid_list;
	}

	public Map<String, Integer[]> get_original_event_list(){
		return this.original_event_list;
	}
	
	public Map<String, Integer[]> get_new_event_list(){
		return this.new_event_list;
	}
	
}

