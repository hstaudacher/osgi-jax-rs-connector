/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Holger Staudacher - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.sse.example;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

@Path( "events" )
public class ExampleService {

  @GET
  @Produces( SseFeature.SERVER_SENT_EVENTS )
  public EventOutput getServerSentEvents() {
    final EventOutput eventOutput = new EventOutput();
    new Thread( new Runnable() {

      @Override
      public void run() {
        try {
          for( int i = 0; i < 10; i++ ) {
            Thread.sleep( 1000 );
            final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
            eventBuilder.name( "message-to-client" );
            eventBuilder.data( String.class, "Hello world " + i + "!" );
            final OutboundEvent event = eventBuilder.build();
            eventOutput.write( event );
          }
        } catch( IOException e ) {
          throw new RuntimeException( "Error when writing the event.", e );
        } catch( InterruptedException e ) {
          e.printStackTrace();
        } finally {
          try {
            eventOutput.close();
          } catch( IOException ioClose ) {
            throw new RuntimeException( "Error when closing the event output.", ioClose );
          }
        }
      }
    } ).start();
    return eventOutput;
  }
}
