package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Agent {
	public static final double EPS = 0.0001;
	private static int _count = 0;
	private static int _seed = 3331;
	private int _id;
	private geo_location _pos;
	private double _speed;
	private edge_data _curr_edge;
	private node_data _curr_node;
	private directed_weighted_graph _gg;
	private My_Pokemon pokemonTarget;
	private long _sg_dt;
	private double _value;

	// Constructor
	public CL_Agent(directed_weighted_graph g, int start_node) {
		_gg = g;
		setMoney(0);
		this._curr_node = _gg.getNode(start_node);
		_pos = _curr_node.getLocation();
		_id = -1;
		setSpeed(0);
	}

	/**
	 * Update the agent
	 * @param json - getting the details of the agent from the server
	 */
	public void update(String json) {
		JSONObject line;
		try {
			line = new JSONObject(json);
			JSONObject ttt = line.getJSONObject("Agent");
			int id = ttt.getInt("id");
			if (id == this.getID() || this.getID() == -1) {
				if (this.getID() == -1) {
					_id = id;
				}
				double speed = ttt.getDouble("speed");
				String p = ttt.getString("pos");
				Point3D pp = new Point3D(p);
				int src = ttt.getInt("src");
				int dest = ttt.getInt("dest");
				double value = ttt.getDouble("value");
				this._pos = pp;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getSrcNode() {
		return this._curr_node.getKey();
	}

	public String toJSON() {
		int d = this.getNextNode();
		return "{\"Agent\":{"
				+ "\"id\":" + this._id + ","
				+ "\"value\":" + this._value + ","
				+ "\"src\":" + this._curr_node.getKey() + ","
				+ "\"dest\":" + d + ","
				+ "\"speed\":" + this.getSpeed() + ","
				+ "\"pos\":\"" + _pos.toString() + "\""
				+ "}"
				+ "}";
	}

	/**
	 * setting the value of agent
	 * @param v - value
	 */
	private void setMoney(double v) {
		_value = v;
	}

	/**
	 * Setting the next node for the agent
	 * @param dest - target node
	 */
	public void setNextNode(int dest) {
		int src = this._curr_node.getKey();
		this._curr_edge = _gg.getEdge(src, dest);
	}

	/**
	 * Setting the current node for the agent
	 * @param src - source node
	 */
	public void setCurrNode(int src) {
		this._curr_node = _gg.getNode(src);
	}

	/**
	 * @return true if agent is moving
	 */
	public boolean isMoving() {
		return this._curr_edge != null;
	}

	public String toString() {
		return toJSON();
	}

	/**
	 * @return the id of the agent
	 */
	public int getID() {
		return this._id;
	}

	public geo_location getLocation() {
		return _pos;
	}


	public double getValue() {
		return this._value;
	}

	/**
	 * getting the next node for the agent
	 * @return target node
	 */
	public int getNextNode() {
		int ans = -2;
		if (this._curr_edge == null) {
			ans = -1;
		} else {
			ans = this._curr_edge.getDest();
		}
		return ans;
	}

	public double getSpeed() {
		return this._speed;
	}

	public void setSpeed(double v) {
		this._speed = v;
	}

	public My_Pokemon getPokemonTarget() {
		return pokemonTarget;
	}

	public void setPokemonTarget(My_Pokemon pokemonTarget) {
		this.pokemonTarget = pokemonTarget;
	}

	public edge_data get_curr_edge() {
		return this._curr_edge;
	}
}
