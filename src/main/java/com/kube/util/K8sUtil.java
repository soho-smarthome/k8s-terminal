package com.kube.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.auth.ApiKeyAuth;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;

public class K8sUtil {
	
	/**
	 * 在使用前要先调用该方法,设置k8s集群的配置
	 * 
	 * @param kubeConfigPath
	 * @return
	 * @throws IOException
	 * @throws ApiException 
	 */
	public static void setConfig(String kubeConfigPath) throws IOException, ApiException {
		ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
		Configuration.setDefaultApiClient(client);
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth BearerToken = (ApiKeyAuth) defaultClient.getAuthentication("BearerToken");
		BearerToken.setApiKey("eyJhbGciOiJSUzI1NiIsImtpZCI6IkV5Zkp3elg5SVBvcDBIb05fRVRMWEdKUVE3R09oVGtiS0ZndmUwWHk3SzQifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImRlZmF1bHQtdG9rZW4ta3pmcGgiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGVmYXVsdCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjZmODRiZTM5LTAxNTMtNGVmMS05NDdlLTliZGZhNjI2YmQ3OSIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDpkZWZhdWx0OmRlZmF1bHQifQ.QX7QB1FfoL12QXnQp-hHMT63jnSv9SSwXRo8th6IIpQwjjjTL3YP2K7mKwsch83AeKC-rXckHAsIGtG4Fejvpz9kGV9PzDoazE7ISErKf8X977VYjL_d2qYhuVwmKev3Yvf-7bWdL86UBsfBoPMUbSvsg4aFVG7pRu6OADPB2cnQMNtGmwzdCmscI1q0JliVHlvBbVB2G0OipN3v9poc60wZafK6fxV6i_b73vK9Sjx1Yd4KClLAbMCTRXHi0PhNvqbILA2CPGShlB0GAIPmVS5KvbkTsadLardnzn-3a1ewdD-KXB5Nw2AmOBIZhTHDg4Iu3FcQgmSfGYgnXtfpMw");

		CoreV1Api api = new CoreV1Api();
		V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
		for (V1Pod item : list.getItems()) {
			System.out.println(item.getMetadata().getName());
		}
	}

	/**
	 * 加载yaml配置文件
	 *
	 * @param path
	 * @throws IOException
	 */
	public static Object loadYaml(String path) throws IOException {
		Reader reader = new FileReader(path);
		return Yaml.load(reader);
	}

	/**
	 * 创建pod
	 *
	 * @param nameSpace
	 *            ：名称空间
	 * @param body
	 *            ：pod
	 * @return
	 * @throws ApiException
	 */
	public static V1Pod createPod(String nameSpace, V1Pod body) throws ApiException {

		return new CoreV1Api().createNamespacedPod(nameSpace, body, "true", "true", null);

	}

	/**
	 * 删除pod
	 *
	 * @param nameSpace
	 * @param podName
	 * @throws Exception
	 */
	public static void deletePod(String nameSpace, String podName) throws Exception {
		new CoreV1Api().deleteNamespacedPod(podName, nameSpace, "true", null, null, null, null, null);
	}

	/**
	 * 创建service
	 *
	 * @param nameSpace
	 * @param sv
	 * @throws ApiException
	 */
	public static void createService(String nameSpace, V1Service sv) throws ApiException {
		new CoreV1Api().createNamespacedService(nameSpace, sv, "true", "true", null);
	}

	/**
	 * 删除service
	 *
	 * @param nameSpace
	 * @param serviceName
	 * @throws Exception
	 */
	public static void deleteService(String nameSpace, String serviceName) throws Exception {
		new CoreV1Api().deleteNamespacedService(serviceName, nameSpace, null, null, null, null, null, null);
	}

	/**
	 * 创建deployment
	 *
	 * @param nameSpace
	 * @param body
	 * @throws ApiException
	 */
	public static void createDeployment(String nameSpace, V1Deployment body) throws ApiException {
		new AppsV1Api().createNamespacedDeployment(nameSpace, body, "true", "true", null);
	}

	/**
	 * 刪除namespace
	 *
	 * @param nameSpace
	 * @param deployeName
	 * @throws ApiException
	 */
	public static void deleteDeployment(String nameSpace, String deployeName) throws ApiException {
		new AppsV1Api().deleteNamespacedDeployment(deployeName, nameSpace, "true", null, null, null, null, null);
	}

	public static void main(String[] args) throws Exception {
		setConfig("D:\\git\\config");
		Reader reader = new FileReader("datax3.yaml");
		Object load = Yaml.load(reader);
		System.out.println(load.getClass());
		V1Deployment d = (V1Deployment) load;
		d.getMetadata().setName("datax-test");
		deleteDeployment("default", d.getMetadata().getName());

	}
}
