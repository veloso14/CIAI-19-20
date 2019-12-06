import React from 'react';
import Container from "react-bootstrap/Container";
import {useParams} from "react-router";
import {GlobalState} from "../App";
import {Appointment} from "./AppointmentList";
import {connect} from "react-redux";
import {getData} from "../Utils/NetworkUtils";
import {Vet} from "./VetList";
import Card from "react-bootstrap/Card";
import useForm from "react-hook-form";
import {updateAppointmentRequest} from "../actions/AppointmentActions";


type FormData = {
    desc: string;
}



const AppointmentDetails = (props: { apt: Appointment,appointments: Appointment[], updateAppointment: (id: string, desc: String) => void }) => {
    let {id} = useParams();
    const [apt, setApt] = React.useState({} as Appointment)
    const [loading, setLoading] = React.useState(false)
    const [vet, setVet] = React.useState({} as Vet)

    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({desc}) => {
        console.log(desc);
        props.updateAppointment(id as string, desc)
        setValue("desc", "")
    });

    const loadAppointment = (id: string) => {
        setLoading(true)
        return getData(`/appointments/${id}`, {} as Appointment)
            .then(data => {
                console.log("fetch appointment: " + JSON.stringify(data))
                setApt(data)
                setLoading(false)
            })
    };


    const loadVet = (id: string) => {
        setLoading(true)
        return getData(`/vets/${+id}`, {} as Vet)
            .then(data => {
                console.log("log: " + JSON.stringify(data))
                data && setVet(data)
                setLoading(false)
            })

    }


    React.useEffect(() => {
        loadAppointment(id as string)
    }, [id])

    React.useEffect(() => {
            if (typeof apt.vetID != "undefined") {
                loadVet(apt.vetID)
            }
        }, [apt]
    )

    let date = String(apt.date)
    let desc = String(apt.desc)

    let content = (
        <Container>
        <div>
            <h1 className="text-center">Appointment Details:</h1>
            <br/>
            <h3>Completed: {desc.length > 0 ? <span>True</span> : <span>False</span>}</h3>
            {desc.length > 0 && <h3>Vet desc: {desc}</h3>}
            <h3>Date: {date.toString().slice(0, 10)}</h3>
            <h3>Time: {date.toString().slice(11, 19)}</h3>
            <h3>Vet: {vet.name}</h3>
        </div>
            <br/>
            <br/>
            <Card>
                <Card.Header>

                    <h1 className="text-center">Complete appointment</h1>

                </Card.Header>
                <Card.Body>

                    <form onSubmit={onSubmit}>
                        <div className="form-group">
                            <label>Description</label>
                            <input className="form-control" id="desc" name="desc"
                                   ref={register({required: true})}/>
                            {errors.desc && 'desc is required'}
                        </div>
                        <input className="btn btn-primary float-right" type="submit" value="Complete appointment"/>
                    </form>
                </Card.Body>

            </Card>
        </Container>

    )

    let loadingContent = (
        <div>
            <h1>Loading...</h1>
        </div>
    )

    return (
        <Container className="m-4">
            {loading ? loadingContent : content}
        </Container>
    );
}


const mapStateToProps = (state: GlobalState) => {
    return {
        apt: state.appointments.apt,
        appointments: state.appointments.appointments,
        isFetching: state.appointments.isFetching
    }
};
const mapDispatchToProps = (dispatch: any) => {
    return {
        updateAppointment: (id: string, desc: String) => {
            dispatch(updateAppointmentRequest(id, desc))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(AppointmentDetails);