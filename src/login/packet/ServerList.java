package login.packet;

import kernel.Console;
import login.LoginClient;
import object.Account;
import object.Player;
import object.Server;

class ServerList {

    public static void get(LoginClient client) {
        client.send("AxK" + serverList(client.getAccount()));
    }

    private static String serverList(Account account) {
        StringBuilder sb = new StringBuilder(account.getSubscribeRemaining() + "");

        for (Server server : Server.servers.values()) {
            int i = characterNumber(account, server.getId());
            if (i == 0)
                continue;

            sb.append("|").append(server.getId()).append(",").append(i);
        }

        Console.instance.write("[" + account.getClient().getIoSession().getId() + "] Sending list of server of account name " + account.getName() + ". List : '" + sb.toString() + "'");
        return sb.toString();
    }

    private static int characterNumber(Account account, int server) {
        int i = 0;

        for (Player character : account.getPlayers().values())
            if(character != null)
                if (character.getServer() == server)
                    i++;

        return i;
    }
}
