package com.kube.controller;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Endpoints;
import io.kubernetes.client.openapi.models.V1EndpointsList;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.util.ClientBuilder;

@RestController
public class HelloController {
	
	@Autowired
	ApiClient apiClient;
	
//	@Autowired
//	KubeConfig config;
	
	@GetMapping("hello")
	public String hello() {
		return "hello!";
	}
	
	/**
	 * out of cluster not in pod
	 * @return
	 * @throws ApiException 
	 */
	@GetMapping("/out/hello")
	public String outCluster() throws ApiException {
		Configuration.setDefaultApiClient(apiClient);
		CoreV1Api api = new CoreV1Api();
		V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
		for (V1Pod item : list.getItems()) {
			System.out.println(item.getMetadata().getName());
		}
		System.out.println("---------------------------------------------------------------------------------------");
		V1EndpointsList ends = api.listEndpointsForAllNamespaces(null, null, null, null, null, null, null, null, null);
		for (V1Endpoints end : ends.getItems()) {
			System.out.println(end.getMetadata().getName() + "   ");
			// + end.toString()
		}
		System.out.println("---------------------------------------------------------------------------------------");
		V1ServiceList svcs = api.listServiceForAllNamespaces(null, null, null, null, null, null, null, null, null);
		for (V1Service	svc : svcs.getItems()) {
			System.out.println(svc.getMetadata().getName() + "   ");
			// + svc.toString()
		}
		System.out.println("---------------------------------------------------------------------------------------");
		V1NodeList nodes = api.listNode(null, null, null, null, null, null, null, null, null);
		for (V1Node node : nodes.getItems()) {
			System.out.println(node.getMetadata().getName() + "   " );
			//+ node.toString()
		}
		System.out.println("---------------------------------------------------------------------------------------");
		
		return "out cluster hello!";
	}
	
	/**
	 * in cluster in pod
	 * @return
	 * @throws IOException 
	 */
	@GetMapping("/in/hello")
	public String inCluster() throws IOException {
		ApiClient client = ClientBuilder.cluster().build();
		Configuration.setDefaultApiClient(client);
		// the CoreV1Api loads default api-client from global configuration.
		CoreV1Api api = new CoreV1Api();
		// invokes the CoreV1Api client
		V1PodList list;
		try {
			list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
			for (V1Pod item : list.getItems()) {
				System.out.println(item.getMetadata().getName());
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return "in cluster hello!";
	}

}
