package com.buaa.lottery.compute;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Message;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class ComputeClient {
	private static final Logger logger = Logger.getLogger(ComputeClient.class.getName());

	private final ManagedChannel channel;
	private final ComputeGrpc.ComputeBlockingStub blockingStub;
	private TestHelper testHelper;

	/** Construct client connecting to Executor server at {@code host:port}. */
	public ComputeClient(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port)
				// Channels are secure by default (via SSL/TLS). For the example
				// we disable TLS to avoid
				// needing certificates.
				.usePlaintext(true));
	}

	/**
	 * Construct client for accessing Executor server using the existing
	 * channel.
	 */
	ComputeClient(ManagedChannelBuilder<?> channelBuilder) {
		channel = channelBuilder.build();
		blockingStub = ComputeGrpc.newBlockingStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public String compute(String tx) {

		ComputeRequest request = ComputeRequest.newBuilder().setTransaction(tx).build();
		ResultReply resultreply;
		try {
			resultreply = blockingStub.compute(request);
			System.out.println("result: " + resultreply.getResult() + "delta:" + resultreply.getDelta());
			if (testHelper != null) {
				testHelper.onMessage(request);
			}
		} catch (StatusRuntimeException e) {
			warning("RPC failed: {0}", e.getStatus());
			if (testHelper != null) {
				testHelper.onRpcError(e);
			}
			return "";
		}
		if (resultreply.getResult())
			return resultreply.getDelta();
		else
			return "";

	}
	
	private void info(String msg, Object... params) {
		logger.log(Level.INFO, msg, params);
	}

	private void warning(String msg, Object... params) {
		logger.log(Level.WARNING, msg, params);
	}

	interface TestHelper {
		/**
		 * Used for verify/inspect message received from server.
		 */
		void onMessage(Message message);

		/**
		 * Used for verify/inspect error received from server.
		 */
		void onRpcError(Throwable exception);
	}

	@VisibleForTesting
	void setTestHelper(TestHelper testHelper) {
		this.testHelper = testHelper;
	}
}
