// Copyright (C) 2009 Micah Martin, Inc. All rights reserved.
// Released under the terms of the LGNU Lesser General Public License.

package socketserver;

import java.net.Socket;

public interface SocketServer
{
	public void serve(Socket s);
}