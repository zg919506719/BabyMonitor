// utils/ajax.ts
export class Ajax {
    private baseURL: string

    constructor(baseURL: string = '') {
        this.baseURL = baseURL
    }

    // 设置token
    setToken(token: string) {
        localStorage.setItem('token', token)
    }

    // 通用请求方法
    async request<T>(url: string, options: RequestInit = {}): Promise<T> {
        const token = localStorage.getItem('token')

        const config: RequestInit = {
            headers: {
                'Content-Type': 'application/json',
                ...(token && { 'Authorization': `Bearer ${token}` }),
                ...options.headers,
            },
            ...options,
        }

        const response = await fetch(`${this.baseURL}${url}`, config)

        if (!response.ok) {
            throw new Error(`请求失败: ${response.status}`)
        }

        return response.json()
    }

    // GET请求
    get<T>(url: string): Promise<T> {
        return this.request<T>(url, { method: 'GET' })
    }

    // POST请求
    post<T>(url: string, data: any): Promise<T> {
        return this.request<T>(url, {
            method: 'POST',
            body: JSON.stringify(data),
        })
    }

    // PUT请求
    put<T>(url: string, data: any): Promise<T> {
        return this.request<T>(url, {
            method: 'PUT',
            body: JSON.stringify(data),
        })
    }

    // DELETE请求
    delete<T>(url: string): Promise<T> {
        return this.request<T>(url, { method: 'DELETE' })
    }
}

// 创建实例
export const ajax = new Ajax('/api/v1')