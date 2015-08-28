uniform mat4 u_Matrix;
uniform vec3 u_VectorToLight;

attribute vec3 a_Position;
attribute vec3 a_Normal;
varying vec3 v_Color;

void main()                    
{	
    v_Color = mix(vec3(0.180, 0.467, 0.153),    // A dark green 
                  vec3(0.660, 0.670, 0.680),    // A stony gray 
                  a_Position.y);
	vec3 scaledNormal = a_Normal;
	scaledNormal.y *= 10.0;
	scaledNormal = normalize(scaledNormal);
    float diffuse = max(dot(scaledNormal,u_VectorToLight),0.0);
    diffuse *= 0.3;
	v_Color *= diffuse;

	//float ambient = 0.2; //day
	float ambient = 0.1;//night
	v_Color +=ambient;

    gl_Position = u_Matrix * vec4(a_Position, 1.0);    
}
