package mod.rp.railnet.common.core.clients;

import mod.rp.railnet.common.core.routing.RailNetwork;
import mod.rp.railnet.common.core.routing.RailNetwork.RailNetCommand;

public class PassThroughClient extends RailNetClient {

	public PassThroughClient(RailNetwork network, String name) {
		super(network, name);
		// TODO Auto-generated constructor stub
	}

	public PassThroughClient(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public PassThroughClient(RailNetwork network) {
		super(network);
		// TODO Auto-generated constructor stub
	}

	public PassThroughClient() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean netCommand(RailNetCommand commandID, Object param) {
		// TODO Auto-generated method stub
		return false;
	}

}
