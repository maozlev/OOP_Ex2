package gameClient;

import api.edge_data;
import api.game_service;
import api.node_data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Ex2 implements game_service {
    private api.directed_weighted_graph g;
    private api.dw_graph_algorithms _g;
    private CL_Pokemon Picachu;
    private ArrayList<CL_Agent> agents;

    @Override
    public String getGraph() {
        return _g.toString();
    }

    @Override
    public String getPokemons() {
        return Picachu.toString();// need to be fixed
    }

    @Override
    public String getAgents() {
        JSONArray agent = new JSONArray();
        for (CL_Agent a : agents) {
            agent.put(a.toJSON());
        }
        return String.valueOf(agent);
    }

    @Override
    public boolean addAgent(int start_node) {
        if (g.getNode(start_node)==null) return false;
        agents.add(new CL_Agent(g,start_node));
        return true;
        // need to finish
    }

    @Override
    public long startGame() {
//        try{
//
//        }
        return (new Date().getTime());
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public long stopGame() {
        return 0;
    }

    @Override
    public long chooseNextEdge(int id, int next_node) {
        return 0;
    }

    @Override
    public long timeToEnd() {
        return 0;
    }

    @Override
    public String move() {
        return null;
    }

    @Override
    public boolean login(long id) {
        return false;
    }
}
