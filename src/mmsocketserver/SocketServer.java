//- Copyright �2009 Micah Martin.  All Rights Reserved
//- MMSocketServer and all included source files are distributed under terms of the GNU LGPL.

// Copyright (C) 2009 Micah Martin, Inc. All rights reserved.
// Released under the terms of the LGNU Lesser General Public License.

package mmsocketserver;

import java.net.Socket;

public interface SocketServer
{
	public void serve(Socket s);
}